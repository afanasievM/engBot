package com.bot.engBot.commands;

import com.bot.engBot.User;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.SendBotMessageServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    final static Logger log = Logger.getLogger(StartCommand.class);
    public final static String START_MESSAGE = "Привет. Я Javarush Telegram Bot. Я помогу тебе быть в курсе последних " +
            "статей тех авторов, котрые тебе интересны. Я еще маленький и только учусь.";

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
//        if (!users.containsKey(chatId)) {
//            log.info("NEW USER");
//            User user = new User(update.getMessage().getChat());
//            users.put(chatId,user);
//            jsonDump(USERS_PATH, users);
//            // Java object to JSON file
//        } else log.info("OLD USER");
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
