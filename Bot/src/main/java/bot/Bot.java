package bot;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.StringWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Bot extends TelegramLongPollingBot {
    final private String token;
    final private String botName;
    final private Logger log = Logger.getLogger(Bot.class);
    final int RECONNECT_PAUSE =10000;
    final private String COMMAND_PREFIX = "/";
    private HashMap<Long,User> users = new HashMap<Long,User>();
    final private String USERS_PATH = System.getProperty("user.dir") + "/users.json";

    public Bot (String token, String botName){

        log.info("Bot init");
        this.token = token;
        this.botName = botName;
        usersInitialize();

    }

    public void usersInitialize(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.users = mapper.readValue(new File(USERS_PATH),HashMap.class);
            log.info(this.users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Users initialized");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            log.info(update.toString());
            if (message.startsWith(COMMAND_PREFIX)) commandProcess(update);
            else sendMsg(update.getMessage().getChatId().toString(), message);
        }
    }

    public void commandProcess(Update update){
        Long chatId = update.getMessage().getChat().getId();
        String message = update.getMessage().getText();
        System.out.println(message);

        switch (message){
            case "/start":
                if (!users.containsKey(chatId)) {
                    log.info("NEW USER");
                    User user = new User(update.getMessage().getChat());
                    System.out.println(user);
                    user.addWord("start","start");
                    users.put(update.getMessage().getChat().getId(), user);
                    log.info(users);
                    log.info(users.get(chatId));

                    ObjectMapper mapper = new ObjectMapper();
                    // Java object to JSON file
                    try {
                        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
                        mapper.writeValue(new File(USERS_PATH), users);
                        String usersString = users.toString();

                        System.out.println(usersString);
                        log.info("user's file is dumped in " + USERS_PATH);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else log.info("OLD USER");
                break;
            case "/del":
                log.info(chatId);
                log.info(this.users.get(chatId));
                log.info(this.users);
                log.info(this.users.get(256885839));
                break;
            default:
                log.info("smth wrong    /" + message);
        }
    }
    /**
     * Метод для настройки сообщения и его отправки.
     * @param chatId id чата
     * @param s Строка, которую необходимот отправить в качестве сообщения.
     */
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



    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    @Override
    public String getBotUsername() {

        return botName;
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     * @return token для бота
     */
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