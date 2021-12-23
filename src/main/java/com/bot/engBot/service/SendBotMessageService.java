package com.bot.engBot.service;

public interface SendBotMessageService {
    void sendMessage(Long chatId, String message);
}
