package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class AddCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(AddCommand.class);


    public AddCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/add","");
        String wordToLearn = null;
        String translate = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToLearn = cmdStructure[0].replace("@vocabengbot ", "").trim();
            translate = cmdStructure[1];
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please user correct form: \n/add word:translate");
            return;
        }
        String finalWordToLearn = wordToLearn;
        String finalTranslate = translate;
        vocabularyService.findByWordAndOwnerId(wordToLearn,chatId).ifPresentOrElse(
                word ->{
                    log.info("OLD WORD");
                    sendBotMessageService.sendMessage(chatId, "You have this word\nIf you want you can correct translation by command\n/correct word;translation");
                },
                ()->{
                    log.info("NEW WORD");
                    Vocabulary vocabulary = new Vocabulary();
                    vocabulary.setOwnerId(chatId);
                    vocabulary.setWord(finalWordToLearn);
                    vocabulary.setWordTranslation(finalTranslate);
                    vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                    vocabulary.setActive(true);
                    sendBotMessageService.sendMessage(chatId, "You add new word:" +
                                                        "\nword -> " + finalWordToLearn +
                                                        "\ntranslation -> " + finalTranslate +
                                                        "\nrepeats -> " + VocabularyServiceImpl.REPEATS);
                    vocabularyService.addWord(vocabulary);
                }
        );

    }
}
