package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ShowMyWordsCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ShowMyWordsCommand.class);
    private final int messageSize = 4096;


    public ShowMyWordsCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Vocabulary> vocabularyList = vocabularyService.retrieveAllUserWord(update.getMessage().getFrom().getId());
        String wordsList = "";
        for (Vocabulary word : vocabularyList) {
            String currentLine = String.format("%s: %d\n", word.getWord(), word.getRepeats());
            if (wordsList.toCharArray().length + currentLine.toCharArray().length > messageSize) {
                sendBotMessageService.sendMessage(chatId, wordsList);
                wordsList = "";
            }
            wordsList += currentLine;
        }

        sendBotMessageService.sendMessage(chatId, wordsList);
        String totalMessage = "Total: " + vocabularyList.size();
        sendBotMessageService.sendMessage(chatId, totalMessage);
    }
}
