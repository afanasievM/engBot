package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class StatCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(StatCommand.class);
    public StatCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;

    }

    @Override
    public void execute(Update update) {
        List<BotUser> activeUsers = new ArrayList<>(botUserService.retrieveAllActiveUsers());
        for (BotUser user:activeUsers) {
            log.info(user);
        }
    }
}
