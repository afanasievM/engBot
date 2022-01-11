package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;


public class GetIDCommand implements Command{
    private final SendBotMessageService sendBotMessageService;



    public GetIDCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;

    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Your id is: <b>" + update.getMessage().getFrom().getId().toString() + "</b>");
    }
}
