package com.bot.engBot.bot;


import com.bot.engBot.User;
import com.bot.engBot.Word;
import com.bot.engBot.commands.CommandContainer;
import com.bot.engBot.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Bot extends TelegramLongPollingBot {

    final private Logger log = Logger.getLogger(Bot.class);
    final private String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final CallBackService callBackService;
    @Value("${bot.botName}")
    private String botName;

    @Value("${bot.token}")
    private String token;


    @Autowired
    public Bot (BotUserService botUserService, VocabularyService vocabularyService){

        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), botUserService, vocabularyService);
        this.callBackService = new CallBackServiceImpl(vocabularyService, botUserService, this);
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                log.info(update.toString());
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
        } else if(update.hasCallbackQuery()) {
            callBackService.callBackProcess(update.getCallbackQuery());
        }
    }


//
    @Override
    public String getBotUsername() {

        return botName;
    }


    @Override
    public String getBotToken() {

        return token;
    }

}
