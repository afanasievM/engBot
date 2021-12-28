package com.bot.engBot.bot;


import com.bot.engBot.User;
import com.bot.engBot.Word;
import com.bot.engBot.commands.CommandContainer;
import com.bot.engBot.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Bot extends TelegramLongPollingBot {

//    final private String token;
//    final private String botName;

    final private Logger log = Logger.getLogger(Bot.class);
    final private String COMMAND_PREFIX = "/";
    final private Integer repeats = 5;
    final private String VOCABULARY_PATH = System.getProperty("user.dir") + "/vocabulary.json";
    private HashMap<Long, User> users = new HashMap<Long,User>();
    private ArrayList<Word> words = new ArrayList<>();
    private HashMap<Long,HashMap<Integer,Integer>> vocabulary = new HashMap<>();
    final private Integer wordsCount = 5;
    private final CommandContainer commandContainer;
    private final CallBackService callBackService;
    @Value("${bot.botName}")
    private String botName;

    @Value("${bot.token}")
    private String token;


    @Autowired
    public Bot (BotUserService botUserService, VocabularyService vocabularyService){

        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), botUserService, vocabularyService);
        this.callBackService = new CallBackServiceImpl(vocabularyService, botUserService, this);
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                log.info(update.toString());
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
        } else if(update.hasCallbackQuery()) {
            callBackService.callBackProcess(update.getCallbackQuery());
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
                case "/del":
                    log.info(chatId);
                    log.info(this.users.get(chatId));
                    log.info(this.users);
                    break;

                case "/show_all_words":
                    String allWords = "";
                    for (Word w:words) {
                        allWords += w.getWord() + "\n";
                    }
//                    sendMsg(chatId.toString(),allWords + "\nTotal: " + words.size());
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
//
//    public void callBackProcess(CallbackQuery callBack){
//        log.info(callBack.getMessage());
//        Long chatId = callBack.getMessage().getChat().getId();
//        String wordFromMessage = callBack.getMessage().getText();
//        String wordFromMessageId = callBack.getData();
//        log.info(wordFromMessage);
//        log.info(wordFromMessageId);
//
//        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId.toString(), callBack.getMessage().getMessageId(),callBack.getInlineMessageId(),null);
//
//        try {
//            execute(edit);
//        } catch (TelegramApiException e) {
//            log.info(e);
//        }
//        AnswerCallbackQuery answerCallBack = new AnswerCallbackQuery();
//        answerCallBack.setCallbackQueryId(callBack.getId());
//        answerCallBack.setShowAlert(true);
//        String messageToSend;
//        if (wordFromMessage.equals(words.get(Integer.valueOf(wordFromMessageId)).getWord())){
//            messageToSend = wordFromMessage + " -> " + words.get(Integer.valueOf(wordFromMessageId)).getTranslate() + ".\nTRUE";
//            answerCallBack.setText(messageToSend);
//            try {
//                execute(answerCallBack);
//            } catch (Exception e){
//                log.info(e);
//            }
//            wordsRepeatsDecrease(chatId, Integer.valueOf(wordFromMessageId));
//        } else {
//            messageToSend = "FALSE\n" + wordFromMessage + " repeats reset to " + this.repeats.toString();
//
//            answerCallBack.setText(messageToSend);
//
//            try {
//                execute(answerCallBack);
//            } catch (Exception e){
//                log.info(e);
//            }
//            wordsReset(chatId, wordFromMessage);
//        }
//
//
//    }
//    public void wordsRepeatsDecrease(Long chatID, Integer wordId){
//        HashMap<Integer,Integer> userWords= vocabulary.get(chatID);
//        if (userWords.keySet().contains(wordId)) {
//            userWords.put(wordId, userWords.get(wordId) - 1);
//            if (userWords.get(wordId) == 0) {
//                userWords.remove(wordId);
//                sendMsg(chatID.toString(), "You learned *" + words.get(wordId).getWord() + "* -> *" + words.get(wordId).getTranslate() + "*!!!!");
//            }
//            log.info(vocabulary);
//            jsonDump(VOCABULARY_PATH, vocabulary);
//        } else {
//            log.info("No word: " +  words.get(wordId) + " wordID: " + wordId.toString() + " in user vocabulary: " + chatID.toString());
//            log.info(vocabulary.get(chatID));
//        }
//    }
//    public void wordsReset(Long chatID, String word){
//        Integer wordId = null;
//        for (int i = 0; i < words.size(); i++) {
//            if (word.equals(words.get(i).getWord())){
//                wordId = i;
//                break;
//            }
//        }
//        HashMap<Integer,Integer> userWords= vocabulary.get(chatID);
//        if (userWords.keySet().contains(wordId)) {
//            userWords.put(wordId, this.repeats);
//            log.info(vocabulary);
//            jsonDump(VOCABULARY_PATH, vocabulary);
//        }
//    }
//    public void jsonDump(String path, Object obj){
//        ObjectMapper mapper = new ObjectMapper();
//        // Java object to JSON file
//        try {
//            mapper.writer().withDefaultPrettyPrinter().writeValue(new File(path), obj);
////                    defaultPrettyPrintingWriter().writeValue(new File(path), obj);
//            log.info(obj.getClass().getName() + "'s file is dumped in " + path);
//        } catch (IOException e) {
////            e.printStackTrace();
//            log.info(e.toString());
//        }
//
//    }

//    public synchronized void sendMsg(String chatId, String s) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.enableMarkdown(true);
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(s);
//
//        sendMessage.setParseMode(ParseMode.MARKDOWN);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            log.error("Exception: " +  e.toString());
//        }
//    }




//
    @Override
    public String getBotUsername() {

        return botName;
    }


    @Override
    public String getBotToken() {

        return token;
    }

}
