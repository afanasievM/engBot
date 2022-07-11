package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;


public class RepeatAllWordsCommand extends IdHandler implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(com.bot.engBot.commands.RepeatAllWordsCommand.class);


    public RepeatAllWordsCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        setSenderIdAndChatId(update);
        vocabularyService.retrieveAllUserWord(senderId).forEach(w -> vocabularyService.resetWord(w));
        String message = String.format("All words have been set repeats to %d.\nWrite /show_my_unstudied_words " +
                "to see your words to repeat.", VocabularyServiceImpl.REPEATS);
        sendBotMessageService.sendMessage(senderId, message);
    }

}
