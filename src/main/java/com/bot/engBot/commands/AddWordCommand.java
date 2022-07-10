package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class AddWordCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(AddWordCommand.class);
    private String wordToLearn;
    private String translate;
    private Long chatId;
    private Long senderId;


    public AddWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        parse(update.getMessage().getText());
        if (wordToLearn == null || translate == null) {
            return;
        }
        addWordToUser();
    }

    private void parse(String updateText) {
        String cmd = updateText.replace("/add", "").trim();
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToLearn = cmdStructure[0].replace("@vocabengbot ", "").trim();
            translate = cmdStructure[1].trim();
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add word;translate");
            return;
        }
    }

    private void addWordToUser() {
        vocabularyService.findByWordAndOwnerId(wordToLearn, senderId).ifPresentOrElse(
                word -> {
                    log.info("OLD WORD");
                    sendBotMessageService.sendMessage(chatId, "You have this word\n" +
                            "If you want you can correct translation by command\n" +
                            "/replace_translation word;translation");
                },
                () -> {
                    log.info("NEW WORD");
                    String message = String.format("You add new word:\nword -> %s\ntranslation -> %s\nrepeats -> %d",
                            wordToLearn, translate, VocabularyServiceImpl.REPEATS);
                    sendBotMessageService.sendMessage(chatId, message);
                    Vocabulary vocabulary = new Vocabulary();
                    vocabulary.setOwnerId(senderId);
                    vocabulary.setWord(wordToLearn);
                    vocabulary.setWordTranslation(translate);
                    vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                    vocabulary.setActive(true);
                    vocabularyService.addWord(vocabulary);
                }
        );
    }
}
