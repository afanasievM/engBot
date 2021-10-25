package bot;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.StringWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            log.info(this.words);
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
                    String wordToLearn = cmd[2].split(";")[0];
                    String translate = cmd[2].split(";")[1];
                    Word word = new Word(wordToLearn,translate);
                    if (!words.contains(word)) {
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
                            } else log.info("OLD USER WORD");
                        });
                    } else {
                        log.info("Vocabulary is empty");
                        HashMap<Integer,Integer> userVocabulary = new HashMap<>();
                        userVocabulary.put(words.indexOf(word),this.repeats);
                        vocabulary.put(chatId,userVocabulary);
                        jsonDump(VOCABULARY_PATH, vocabulary);
                    }
                    log.info(vocabulary);
                    sendMsg(chatId.toString(), "word -> " + wordToLearn + " translate -> " + translate + "\nrepeats to learn -> " + this.repeats);
                    break;
                case "/show_my_words":
                    String wordList = "";
                    for (Integer wordId:vocabulary.get(chatId).keySet()) {
                        wordList += words.get(wordId).getWord() + "\n";
                    }
                    sendMsg(chatId.toString(),wordList);
                    break;
                case "/show_all_words":
                    String allWords = "";
                    for (Word w:words) {
                        allWords += w.getWord() + "\n";
                    }
                    sendMsg(chatId.toString(),allWords);
                    break;
                default:
                    log.info("smth wrong    /" + message);
            }
        }catch (Exception e){
            log.info(e.toString());
        }
    }
    public void callBackProcess(CallbackQuery callBack){
        Long chatId = callBack.getMessage().getChat().getId();
        String wordFromMessage = callBack.getMessage().getText();
        String wordFromMessageId = callBack.getData();
        if (wordFromMessage.equals(words.get(Integer.valueOf(wordFromMessageId)).getWord())){
            String messageToSend = wordFromMessage + " -> " + words.get(Integer.valueOf(wordFromMessageId)).getTranslate() + ".\nTRUE";
            sendMsg(chatId.toString(), messageToSend);
            wordsRepeatsDecrease(chatId, Integer.valueOf(wordFromMessageId));
        } else {
            sendMsg(chatId.toString(), "FALSE");
            sendMsg(chatId.toString(), wordFromMessage + "repeats reset to " + this.repeats.toString());
            wordsReset(chatId, wordFromMessage);
        }

    }
    public void wordsRepeatsDecrease(Long chatID, Integer wordId){
        HashMap<Integer,Integer> userWords= vocabulary.get(chatID);
        userWords.put(wordId, userWords.get(wordId) - 1);
        if (userWords.get(wordId) == 0) {
            userWords.remove(wordId);
            sendMsg(chatID.toString(), "You learned this word!!!!");
        }
        log.info(vocabulary);
        jsonDump(VOCABULARY_PATH,vocabulary);
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
        userWords.put(wordId, this.repeats);
        log.info(vocabulary);
        jsonDump(VOCABULARY_PATH,vocabulary);
    }
    public void jsonDump(String path, Object obj){
        ObjectMapper mapper = new ObjectMapper();
        // Java object to JSON file
        try {
            mapper.writeValue(new File(path), obj);
            log.info(obj.getClass().getName() + "'s file is dumped in " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Exception: " +  e.toString());
        }
    }

    public HashMap<Long,Integer> chooseWord(){
        HashMap<Long,Integer> listToSend = new HashMap<>();
        for (Map.Entry entry:vocabulary.entrySet()) {
            HashMap<Integer,Integer> userVocabulary = (HashMap<Integer, Integer>) entry.getValue();
            Random random = new Random();
            Integer truthWord = random.nextInt(userVocabulary.size());
            listToSend.put((Long) entry.getKey(),truthWord);
        }
        return listToSend;
    }

    public void sendWords(HashMap<Long,Integer> choosenWords){
        for (Map.Entry entry:choosenWords.entrySet()) {
            Long chatId = (Long) entry.getKey();
            Integer wordId = (Integer) entry.getValue();
            Word word = words.get(wordId);
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            ArrayList<Integer> wordsForButtons = new ArrayList<>();
            wordsForButtons.add(wordId);
            for (int i = 1; i < 4; i++) {
                Random random = new Random();
                int id = random.nextInt(words.size());
                if (!wordsForButtons.contains(id)){
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
            sendMessage.setText(words.get(wordId).getWord());
            sendMessage.setReplyMarkup(markup);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Exception: " +  e.toString());
            }
//            sendMsg(chatId.toString(),word.getWord() + "->" + word.getTranslate());
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
