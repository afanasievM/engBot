package com.bot.engBot.commands;

import com.bot.engBot.User;
import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.SendBotMessageServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Date;

public class StartCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final static Logger log = Logger.getLogger(StartCommand.class);
    public final static String START_MESSAGE = "Привет. Я Javarush Telegram Bot. Я помогу тебе быть в курсе последних " +
            "статей тех авторов, котрые тебе интересны. Я еще маленький и только учусь.";

    public StartCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        botUserService.findByChatId(chatId).ifPresentOrElse(
                user -> {
                    user.setActive(true);
                    user.setFirst_name(update.getMessage().getChat().getFirstName());
                    user.setFirst_name(update.getMessage().getChat().getUserName());
                    botUserService.save(user);
                    log.info("OLD USER");
                },
                () -> {
                    log.info("NEW USER");
                    BotUser botUser = new BotUser();
                    botUser.setActive(true);
                    botUser.setId(chatId);
                    botUser.setFirst_name(update.getMessage().getChat().getUserName());
                    botUser.setFirst_name(update.getMessage().getChat().getFirstName());
                    botUser.setUser_language("ua");
                    botUser.setRole("user");
                    botUser.setJoin_at(new Timestamp(new Date().getTime()));
                    log.info(new Date().getTime());
                    botUserService.save(botUser);
                });

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);

//        int activeUserCount = botUserService.retrieveAllActiveUsers().size();
//        if (!users.containsKey(chatId)) {
//            log.info("NEW USER");
//            User user = new User(update.getMessage().getChat());
//            users.put(chatId,user);
//            jsonDump(USERS_PATH, users);
//            // Java object to JSON file
//        } else log.info("OLD USER");
//        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}