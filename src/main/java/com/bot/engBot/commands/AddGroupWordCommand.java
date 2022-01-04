package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class AddGroupWordCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(AddGroupWordCommand.class);


    public AddGroupWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
        this.groupService = groupService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/add_group_word","");
        String wordToLearn = null;
        String translate = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToLearn = cmdStructure[0].replace("@vocabengbot ", "").trim();
            translate = cmdStructure[1].trim();
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group_word word;translate");
            return;
        }
        String finalWordToLearn = wordToLearn;
        String finalTranslate = translate;
        vocabularyService.findByWordAndOwnerId(wordToLearn,chatId).ifPresentOrElse(
                word ->{
                    log.info("OLD WORD");
                    sendBotMessageService.sendMessage(chatId, "You have this word\nIf you want you can correct translation by command\n/replace_translation word;translation");
                },
                ()->{
                    log.info("NEW WORD");
                    sendBotMessageService.sendMessage(chatId, "You add new word:" +
                            "\nword -> " + finalWordToLearn +
                            "\ntranslation -> " + finalTranslate +
                            "\nrepeats -> " + VocabularyServiceImpl.REPEATS);
                    Vocabulary vocabulary = new Vocabulary();
                    vocabulary.setOwnerId(chatId);
                    vocabulary.setWord(finalWordToLearn);
                    vocabulary.setWordTranslation(finalTranslate);
                    vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                    vocabulary.setActive(true);
                    vocabularyService.addWord(vocabulary);
                }
        );

    }
}
