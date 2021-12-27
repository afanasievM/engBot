package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    final static Logger log = Logger.getLogger(UnknownCommand.class);


    public static final String UNKNOWN_MESSAGE = "I don't understand this comman.\n" +
            "Try /help to show commands";

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), UNKNOWN_MESSAGE);
    }
}
