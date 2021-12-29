package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class RemoveWordCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(RemoveWordCommand.class);


    public RemoveWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/remove_word","").replace("@vocabengbot","");
        String wordToRemove = cmd.trim();
        if (!wordToRemove.equals("")){
            vocabularyService.findByWordAndOwnerId(wordToRemove,chatId).ifPresentOrElse(
                    word ->{
                        log.info("Word is present");
                        vocabularyService.removeWord(word);
                        sendBotMessageService.sendMessage(chatId, "You removed <b>" + word.getWord() + " -> " + word.getWordTranslation() + "</b>");
                    },
                    ()->{
                        log.info("Can't find this word");
                        sendBotMessageService.sendMessage(chatId, "Can't find <b>" + wordToRemove + "</b>\nTry command /show_my_words to find your word");
                    }
            );
        }else {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/remove_word word");
        }

    }
}