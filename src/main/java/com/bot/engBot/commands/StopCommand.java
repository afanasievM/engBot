package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.SendBotMessageServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    final static Logger log = Logger.getLogger(StopCommand.class);
    public final static String START_MESSAGE = "Деактивировал все ваши подписки \\uD83D\\uDE1F.";

    public StopCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {

        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
