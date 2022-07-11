package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class RemoveWordCommand implements Command {
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
        Long senderId = update.getMessage().getFrom().getId();
        String wordToRemove = update.getMessage().getText().replace("/remove_word", "")
                .replace("@vocabengbot", "").trim();
        if (wordToRemove.isEmpty()) {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/remove_word word");
        }
        vocabularyService.findByWordAndOwnerId(wordToRemove, senderId).ifPresentOrElse(
                word -> {
                    log.info("Word is present");
                    vocabularyService.removeWord(word);
                    String message = String.format("You removed <b>%s -> %s</b>",
                            word.getWord(), word.getWordTranslation());
                    sendBotMessageService.sendMessage(chatId, message);
                },
                () -> {
                    log.info("Can't find this word");
                    String message = String.format("Can't find <b>%s</b>\nTry command /show_my_words to find your word",
                            wordToRemove);
                    sendBotMessageService.sendMessage(chatId, message);
                }
        );
    }
}
