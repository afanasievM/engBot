package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    final private Logger log = Logger.getLogger(NoCommand.class);


    public static final String NO_MESSAGE = "I support commands that starts with the slash (/).\n"
            + "To view a list of commands write /help";

    public NoCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), NO_MESSAGE);
    }
}
