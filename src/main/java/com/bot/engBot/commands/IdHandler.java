package com.bot.engBot.commands;


import org.telegram.telegrambots.meta.api.objects.Update;

public class IdHandler {

    protected Long senderId;

    protected Long chatId;

    protected void setSenderIdAndChatId(Update update){
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
    }
}
