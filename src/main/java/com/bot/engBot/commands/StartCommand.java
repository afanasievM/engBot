package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Date;

public class StartCommand implements Command{
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
                    botUser.setUsername(update.getMessage().getChat().getUserName());
                    botUser.setFirst_name(update.getMessage().getChat().getFirstName());
                    botUser.setUser_language("ua");
                    botUser.setRole("user");
                    botUser.setJoin_at(new Timestamp(new Date().getTime()));
                    log.info(new Date().getTime());
                    botUserService.save(botUser);
                });

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);

    }


}
