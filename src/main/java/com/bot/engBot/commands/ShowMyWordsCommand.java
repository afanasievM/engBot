package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ShowMyWordsCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ShowMyWordsCommand.class);


    public ShowMyWordsCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Vocabulary> vocabularyList = new ArrayList<>();
        vocabularyList.addAll(vocabularyService.retrieveAllUserWord(chatId));
        String wordsList = "";
        for (Vocabulary word:vocabularyList) {
            wordsList += word.getWord() + "\n";
        }
        wordsList+="Total: " + vocabularyList.size();
        sendBotMessageService.sendMessage(chatId,wordsList);
    }
}
