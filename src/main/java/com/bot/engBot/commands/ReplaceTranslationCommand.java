package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class ReplaceTranslationCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ReplaceTranslationCommand.class);
    private Long chatId;
    private Long senderId;
    private String word;
    private String translationForReplace;


    public ReplaceTranslationCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        parse(update.getMessage().getText());
        if (!isValid(word) || !isValid(translationForReplace)) {
            return;
        }
        vocabularyService.findByWordAndOwnerId(word, senderId).ifPresentOrElse(
                w -> {
                    log.info("Word is present");
                    String message = String.format("You replace <b>%s</b> on <b>%s</b> of <b>%s</b>",
                            w.getWordTranslation(), translationForReplace, word);
                    sendBotMessageService.sendMessage(chatId, message);
                    w.setWordTranslation(translationForReplace);
                    vocabularyService.save(w);
                },
                () -> {
                    log.info("Can't find this word");
                    String message = String.format("Can't find <b>%s</b>\nTry command /show_my_words to find your word",
                            word);
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
            word = cmdStructure[0].trim();
            translationForReplace = cmdStructure[1].trim();
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/replace_translation word;new translation");
        }
    }

    private boolean isValid(String line) {
        if (line == null) {
            return false;
        }
        return !line.isEmpty();
    }
}
