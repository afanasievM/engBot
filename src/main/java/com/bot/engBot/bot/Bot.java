package com.bot.engBot.bot;


import com.bot.engBot.commands.CommandContainer;
import com.bot.engBot.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public Bot (BotUserService botUserService, VocabularyService vocabularyService, GroupService groupService){

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
