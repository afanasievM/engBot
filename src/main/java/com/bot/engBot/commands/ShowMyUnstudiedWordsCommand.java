package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ShowMyUnstudiedWordsCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ShowMyUnstudiedWordsCommand.class);
    private final int messageSize = 4096;

    public ShowMyUnstudiedWordsCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Vocabulary> vocabularyList = new ArrayList<>();
        vocabularyList.addAll(vocabularyService.findAllByOwnerIdAndActiveFalse(update.getMessage().getFrom().getId()));
        String wordsList = "";
        for (Vocabulary word:vocabularyList) {
            String currentLine = word.getWord() + ": " + word.getRepeats() +"\n";
            if (wordsList.toCharArray().length + currentLine.toCharArray().length > messageSize){
                sendBotMessageService.sendMessage(chatId,wordsList);
                wordsList = "";
            }
            wordsList += currentLine;
        }
        sendBotMessageService.sendMessage(chatId,wordsList);
        String totalMessage = "Total: " + vocabularyList.size();
        sendBotMessageService.sendMessage(chatId,totalMessage);
    }
}
