package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class ReplaceTranslationCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ReplaceTranslationCommand.class);


    public ReplaceTranslationCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long senderId = update.getMessage().getFrom().getId();
        String cmd = update.getMessage().getText().replace("/replace_translation","").replace("@vocabengbot","");
        String word = null;
        String translationForReplace = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            word = cmdStructure[0].trim();
            translationForReplace = cmdStructure[1].trim();
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/replace_translation word;new translation");
            return;
        }
        if (!word.equals("") && !translationForReplace.equals("")){
            String finalTranslationForReplace = translationForReplace;
            String finalWord = word;
            vocabularyService.findByWordAndOwnerId(word,senderId).ifPresentOrElse(
                    w ->{
                        log.info("Word is present");
                        sendBotMessageService.sendMessage(chatId, "You replace <b>" + w.getWordTranslation() + "</b> on <b>" + finalTranslationForReplace + "</b> of <b>" + finalWord + "</b>");
                        w.setWordTranslation(finalTranslationForReplace);
                        vocabularyService.save(w);
                    },
                    ()->{
                        log.info("Can't find this word");
                        sendBotMessageService.sendMessage(chatId, "Can't find <b>" + finalWord + "</b>\nTry command /show_my_words to find your word");
                    }
            );
        }else {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/replace_translation word;new translation");
        }

    }
}
