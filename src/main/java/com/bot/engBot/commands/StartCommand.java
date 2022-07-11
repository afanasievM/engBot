package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Date;

public class StartCommand extends IdHandler implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(StartCommand.class);
    public final static String START_MESSAGE = "Welcome to bot. This bot will help you to learn foreign words.\nType /help for see commands";

    public StartCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        setSenderIdAndChatId(update);
        botUserService.findByChatId(senderId).ifPresentOrElse(
                user -> {
                    oldUserProcess(user, update);
                },
                () -> {
                    newUserProcess(update);
                });
        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }

    private void oldUserProcess(BotUser user, Update update) {
        user.setActive(true);
        user.setFirst_name(update.getMessage().getFrom().getFirstName());
        user.setUsername(update.getMessage().getFrom().getUserName());
        botUserService.save(user);
        log.info("OLD USER");
    }

    private void newUserProcess(Update update) {
        log.info("NEW USER");
        BotUser botUser = new BotUser();
        botUser.setActive(true);
        botUser.setId(senderId);
        botUser.setUsername(update.getMessage().getFrom().getUserName());
        botUser.setFirst_name(update.getMessage().getFrom().getFirstName());
        botUser.setUser_language("ua");
        botUser.setRole("user");
        botUser.setJoin_at(new Timestamp(new Date().getTime()));
        log.info(new Date().getTime());
        botUserService.save(botUser);
    }

}
