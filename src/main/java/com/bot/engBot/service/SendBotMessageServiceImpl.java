package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService{
    private final Bot engBot;
    final static Logger log = Logger.getLogger(SendBotMessageServiceImpl.class);

    @Autowired
    public SendBotMessageServiceImpl(Bot engBot) {
        this.engBot = engBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            engBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            log.error(e);
        }
    }
}
