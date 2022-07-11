package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;


public class RepeatWordCommand extends IdHandler implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(RepeatWordCommand.class);

    private String wordToRepeat;


    public RepeatWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        setSenderIdAndChatId(update);
        wordToRepeat = update.getMessage().getText().replace("/repeat_word", "").trim()
                .replace("@vocabengbot", "").trim();
        if (wordToRepeat.isEmpty()) {
            String message = "Please use correct form: \n/repeat_word word";
            sendBotMessageService.sendMessage(senderId, message);
            return;
        }
        findWordAndReset();
    }

    private void findWordAndReset() {
        vocabularyService.findByWordAndOwnerId(wordToRepeat, senderId).ifPresentOrElse(
                word -> {
                    word.setRepeats(VocabularyServiceImpl.REPEATS);
                    log.info("OLD WORD");
                    String message = String.format("Word <b>%s</b> has been changed repeats to %d",
                            word.getWord(), VocabularyServiceImpl.REPEATS);
                    sendBotMessageService.sendMessage(chatId, message);
                    vocabularyService.resetWord(word);
                },
                () -> {
                    log.info("Can't find WORD");
                    String message = String.format("Can't find word <b>%s</b>\nTry use /show_my_unstudied_words" +
                            "to find your correct word", wordToRepeat);
                    sendBotMessageService.sendMessage(chatId, message);
                }
        );
    }


}