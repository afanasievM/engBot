package com.bot.engBot.commands;

import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class ReplaceWord implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ReplaceWord.class);


    public ReplaceWord(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/replace_word","").replace("@vocabengbot","");
        String wordToReplace = null;
        String wordForReplace = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToReplace = cmdStructure[0].trim();
            wordForReplace = cmdStructure[1].trim();
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/replace_word old word;new word");
            return;
        }
        if (!wordToReplace.equals("") && !wordForReplace.equals("")){
            String finalWordForReplace = wordForReplace;
            String finalWordToReplace = wordToReplace;
            vocabularyService.findByWordAndOwnerId(wordToReplace,chatId).ifPresentOrElse(
                    word ->{
                        log.info("Word is present");
                        sendBotMessageService.sendMessage(chatId, "You replace <b>" + finalWordToReplace + "</b> on <b>" + finalWordForReplace + "</b>");
                        word.setWord(finalWordForReplace);
                        vocabularyService.save(word);
                    },
                    ()->{
                        log.info("Can't find this word");
                        sendBotMessageService.sendMessage(chatId, "Can't find <b>" + finalWordToReplace + "</b>\nTry command /show_my_words to find your word");
                    }
            );
        }else {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/replace_word old word;new word");
        }

    }
}
