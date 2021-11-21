package bot;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot extends TelegramLongPollingBot {
    final private String token;
    final private String botName;
    final private Logger log = Logger.getLogger(Bot.class);
    final int RECONNECT_PAUSE =10000;
    final private String COMMAND_PREFIX = "/";
    final private Integer repeats = 5;
    final private String USERS_PATH = System.getProperty("user.dir") + "/users.json";
    final private String WORDS_PATH = System.getProperty("user.dir") + "/words.json";
    final private String VOCABULARY_PATH = System.getProperty("user.dir") + "/vocabulary.json";
    private HashMap<Long,User> users = new HashMap<Long,User>();
    private ArrayList<Word> words = new ArrayList<>();
    private HashMap<Long,HashMap<Integer,Integer>> vocabulary = new HashMap<>();
    final private Integer wordsCount = 5;


    public Bot (String token, String botName){

        log.info("Bot init");
        this.token = token;
        this.botName = botName;
        usersInitialize();
        vocabularyInitialize();
        wordsInitialize();

    }

    public void usersInitialize(){
        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

        TypeReference<HashMap<Long,User>> typeRef = new TypeReference<HashMap<Long,User>>() {};
        try {
            this.users.putAll(mapper.readValue(new File(USERS_PATH),typeRef));
            log.info(this.users);
            log.info("Users initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void wordsInitialize(){
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ArrayList<Word>> typeRef = new TypeReference<ArrayList<Word>>() {};
        try {
            this.words.addAll(mapper.readValue(new File(WORDS_PATH),typeRef));
            log.info(this.words + "\nSize: " + this.words.size());
            log.info("Words initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void vocabularyInitialize(){
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Long,HashMap<Integer,Integer>>> typeRef = new TypeReference<HashMap<Long,HashMap<Integer,Integer>>>() {};
        try {
            this.vocabulary.putAll(mapper.readValue(new File(VOCABULARY_PATH),typeRef));
            log.info(this.vocabulary);
            log.info("Vocabulary initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            log.info(update.toString());
            log.info(update.getMessage().getText());
            System.out.println(update.getMessage().getText());
            if (message.startsWith(COMMAND_PREFIX)) commandProcess(update);
//            else sendMsg(update.getMessage().getChatId().toString(), message);
        } else if(update.hasCallbackQuery()) {
            callBackProcess(update.getCallbackQuery());
        }
    }

    public void commandProcess(Update update){
        Long chatId = update.getMessage().getChat().getId();
        String message = update.getMessage().getText();
        Pattern pattern = Pattern.compile("(/\\w*)\\s*(.*)");
        Matcher m = pattern.matcher(message);
        List<String> result = new ArrayList<>();
        if (m.find()){
            log.info("find");
            for (int i = 0; i <= m.groupCount(); i++) {
                log.info(m.group(i));
                result.add(m.group(i));
            }
        }
        String[] cmd = result.stream().toArray(String[]::new);
        try {
            switch (cmd[1]) {
                case "/start":
                    if (!users.containsKey(chatId)) {
                        log.info("NEW USER");
                        User user = new User(update.getMessage().getChat());
                        users.put(chatId,user);
                        jsonDump(USERS_PATH, users);
                        // Java object to JSON file
                    } else log.info("OLD USER");
                    break;
                case "/del":
                    log.info(chatId);
                    log.info(this.users.get(chatId));
                    log.info(this.users);
                    break;
                case "/add":
                    boolean wordContains = false;
                    String wordToLearn = null;
                    String translate = null;
                    try {
                        wordToLearn = cmd[2].split(";")[0].replace("@vocabengbot ", "").trim();
                        translate = cmd[2].split(";")[1];
                    } catch (Exception e){
                        log.info(e);
                        sendMsg(chatId.toString(), "Please user correct form: \n/add word:translate");
                        break;
                    }
                    Word word = new Word(wordToLearn,translate);
                    for (Word w:words) {
//                        log.info(w.getWord() + " comapare " + wordToLearn);
                        if (w.getWord().equals(wordToLearn)){
                            wordContains = true;
                            break;
                        }
                    }
                    log.info("contain word " + wordContains);
                    if (!wordContains) {
                        this.words.add(word);
                        log.info("NEW WORD");
                        jsonDump(WORDS_PATH, words);
                    } else log.info("OLD WORD");
                    if (!vocabulary.isEmpty()) {
                        vocabulary.forEach((k, v) -> {
                            if (!v.containsKey(words.indexOf(word))) {
                                log.info("NEW USER WORD");
                                v.put(words.indexOf(word), this.repeats);
                                jsonDump(VOCABULARY_PATH, vocabulary);
                            } else {
                                log.info("OLD USER WORD");

                            }
                        });
                    } else {
                        log.info("Vocabulary is empty");

                        for (Map.Entry<Long,User> entry:users.entrySet()) {
                            Long userID = entry.getKey();
                            HashMap<Integer,Integer> userVocabulary = new HashMap<>();
                            userVocabulary.put(words.indexOf(word),this.repeats);
                            vocabulary.put(userID,userVocabulary);
                            jsonDump(VOCABULARY_PATH, vocabulary);
                        }



                    }
                    log.info(vocabulary);
                    sendMsg(chatId.toString(), "word -> " + wordToLearn + " translate -> " + translate + "\nrepeats to learn -> " + this.repeats);
                    break;
                case "/show_my_words":
                    String wordList = "";
                    for (Integer wordId:vocabulary.get(chatId).keySet()) {
                        wordList += words.get(wordId).getWord() + "\n";
                    }
                    sendMsg(chatId.toString(),wordList + "\nTotal: " + vocabulary.get(chatId).keySet().size());
                    break;
                case "/show_all_words":
                    String allWords = "";
                    for (Word w:words) {
                        allWords += w.getWord() + "\n";
                    }
                    sendMsg(chatId.toString(),allWords + "\nTotal: " + words.size());
                    break;
                case "/rebuild_vocabulary":
                    for (Map.Entry<Long,HashMap<Integer,Integer>> entry:vocabulary.entrySet()) {
                        entry.getValue().clear();
                        for (int i = 0; i < words.size(); i++) {
                            entry.getValue().put(i,this.repeats);
                        }
                    }
                    log.info(vocabulary);
                    break;
                default:
                    log.info("smth wrong    /" + message);
            }
        }catch (Exception e){
            log.info(e.toString());
        }
    }
    public void callBackProcess(CallbackQuery callBack){
        log.info(callBack.getMessage());
        Long chatId = callBack.getMessage().getChat().getId();
        String wordFromMessage = callBack.getMessage().getText();
        String wordFromMessageId = callBack.getData();
        log.info(wordFromMessage);
        log.info(wordFromMessageId);

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId.toString(), callBack.getMessage().getMessageId(),callBack.getInlineMessageId(),null);

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            log.info(e);
        }
//        public synchronized void answerCallbackQuery(String callbackId, String message) {
//            AnswerCallbackQuery answer = new AnswerCallbackQuery();
//            answer.setCallbackQueryId(callbackId);
//            answer.setText(message);
//            answer.setShowAlert(true);
//            try {
//                answerCallbackQuery(answer);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
        AnswerCallbackQuery answerCallBack = new AnswerCallbackQuery();
        answerCallBack.setCallbackQueryId(callBack.getId());
        answerCallBack.setShowAlert(true);
//        answerCallBack.set
//        answerCallBack
        String messageToSend;
        if (wordFromMessage.equals(words.get(Integer.valueOf(wordFromMessageId)).getWord())){
            messageToSend = wordFromMessage + " -> " + words.get(Integer.valueOf(wordFromMessageId)).getTranslate() + ".\nTRUE";
            answerCallBack.setText(messageToSend);
//            answerCallBack.getText().
//            sendMsg(chatId.toString(), messageToSend);
            try {
                execute(answerCallBack);
            } catch (Exception e){
                log.info(e);
            }
            wordsRepeatsDecrease(chatId, Integer.valueOf(wordFromMessageId));
        } else {
            messageToSend = "FALSE\n" + wordFromMessage + " repeats reset to " + this.repeats.toString();

            answerCallBack.setText(messageToSend);

            try {
                execute(answerCallBack);
            } catch (Exception e){
                log.info(e);
            }
            wordsReset(chatId, wordFromMessage);
        }


    }
    public void wordsRepeatsDecrease(Long chatID, Integer wordId){
        HashMap<Integer,Integer> userWords= vocabulary.get(chatID);
        if (userWords.keySet().contains(wordId)) {
            userWords.put(wordId, userWords.get(wordId) - 1);
            if (userWords.get(wordId) == 0) {
                userWords.remove(wordId);
                sendMsg(chatID.toString(), "You learned *" + words.get(wordId).getWord() + "* -> *" + words.get(wordId).getTranslate() + "*!!!!");
            }
            log.info(vocabulary);
            jsonDump(VOCABULARY_PATH, vocabulary);
        } else {
            log.info("No word: " +  words.get(wordId) + " wordID: " + wordId.toString() + " in user vocabulary: " + chatID.toString());
            log.info(vocabulary.get(chatID));
        }
    }
    public void wordsReset(Long chatID, String word){
        Integer wordId = null;
        for (int i = 0; i < words.size(); i++) {
            if (word.equals(words.get(i).getWord())){
                wordId = i;
                break;
            }
        }
        HashMap<Integer,Integer> userWords= vocabulary.get(chatID);
        if (userWords.keySet().contains(wordId)) {
            userWords.put(wordId, this.repeats);
            log.info(vocabulary);
            jsonDump(VOCABULARY_PATH, vocabulary);
        }
    }
    public void jsonDump(String path, Object obj){
        ObjectMapper mapper = new ObjectMapper();
        // Java object to JSON file
        try {
            mapper.writer().withDefaultPrettyPrinter().writeValue(new File(path), obj);
//                    defaultPrettyPrintingWriter().writeValue(new File(path), obj);
            log.info(obj.getClass().getName() + "'s file is dumped in " + path);
        } catch (IOException e) {
//            e.printStackTrace();
            log.info(e.toString());
        }

    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

        sendMessage.setParseMode(ParseMode.MARKDOWN);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Exception: " +  e.toString());
        }
    }

    public HashMap<Long,ArrayList<Integer>> chooseWord(){
        HashMap<Long,ArrayList<Integer>> listToSend = new HashMap<>();
        for (Map.Entry entry:vocabulary.entrySet()) {
            HashMap<Integer,Integer> userVocabulary = (HashMap<Integer, Integer>) entry.getValue();
            ArrayList<Integer> wordsForUser = new ArrayList<>();
            if (userVocabulary.size() > 0) {
                for (int i = 0; i < wordsCount; i++) {
                    Integer[] keySet = userVocabulary.keySet().toArray(Integer[]::new);
                    Random random = new Random();
                    Integer truthWord = random.nextInt(userVocabulary.size());
                    truthWord = keySet[truthWord];
                    wordsForUser.add(truthWord);
                }
                listToSend.put((Long) entry.getKey(), wordsForUser);
            }
        }
        return listToSend;
    }

    public void sendWords(HashMap<Long,ArrayList<Integer>> choosenWords){
        for (Map.Entry entry:choosenWords.entrySet()) {
            Long chatId = (Long) entry.getKey();
            ArrayList<Integer> wordsId = (ArrayList<Integer>) entry.getValue();
            if (wordsId.size() == 0) continue;
            for (Integer wordId:wordsId) {
                Word word = words.get(wordId);
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                ArrayList<Integer> wordsForButtons = new ArrayList<>();
                wordsForButtons.add(wordId);
                for (int i = 1; i < 4; i++) {
                    Random random = new Random();
                    int id = random.nextInt(words.size());
                    if (!wordsForButtons.contains(id)) {
                        wordsForButtons.add(id);
                    } else {
                        i--;
                    }
                }
                Collections.shuffle(wordsForButtons);
                for (int i = 0; i < wordsForButtons.size(); i++) {
                    int buttonWordId = wordsForButtons.get(i);
                    List<InlineKeyboardButton> button1 = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(words.get(buttonWordId).getTranslate());
                    button.setCallbackData(String.valueOf(buttonWordId));
                    button1.add(button);
                    buttons.add(button1);
                }
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(buttons);

                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(chatId.toString());
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setText("*" + words.get(wordId).getWord() + "*");

                sendMessage.setReplyMarkup(markup);
                log.info(sendMessage);

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    log.error("Exception: " + e.toString());
                }
//            sendMsg(chatId.toString(),word.getWord() + "->" + word.getTranslate());
            }
        }
    }
    @Override
    public String getBotUsername() {

        return botName;
    }


    @Override
    public String getBotToken() {

        return token;
    }


    public void botConnect() {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            log.info("Bot connection");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        }
    }

}
//
//{256885839=bot.User(role=user,
//        Chat(id=256885839, type=private, title=null, firstName=Mykhailo, lastName=null, userName=My_afo, allMembersAreAdministrators=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null),
//        volabulary={{start=start}=10})}
///start
///del
///add
///show_my_words
///show_all_words
