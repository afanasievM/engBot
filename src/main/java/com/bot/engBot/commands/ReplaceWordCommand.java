package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class ReplaceWordCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ReplaceWordCommand.class);
    private Long chatId;
    private Long senderId;
    private String wordToReplace;
    private String wordForReplace;

    public ReplaceWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        parse(update.getMessage().getText());
        if (!isValid(wordToReplace) || !isValid(wordForReplace)) {
            return;
        }
        vocabularyService.findByWordAndOwnerId(wordToReplace, senderId).ifPresentOrElse(
                word -> {
                    log.info("Word is present");
                    String message = String.format("You replace <b>%s</b> on <b>%s</b>",
                            wordToReplace, wordForReplace);
                    sendBotMessageService.sendMessage(chatId, message);
                    word.setWord(wordForReplace);
                    vocabularyService.save(word);
                },
                () -> {
                    log.info("Can't find this word");
                    String message = String.format("Can't find <b>%s</b>\nTry command /show_my_words to find your word",
                            wordToReplace);
                    sendBotMessageService.sendMessage(chatId, message);
                }
        );

    }

    private void parse(String updateText) {
        String cmd = updateText.replace("/replace_translation", "")
                .replace("@vocabengbot", "").trim();
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToReplace = cmdStructure[0].trim();
            wordForReplace = cmdStructure[1].trim();
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n" +
                    "/replace_word old word;new word");
        }
    }

    private boolean isValid(String line) {
        if (line == null) {
            return false;
        }
        return !line.isEmpty();
    }
}
