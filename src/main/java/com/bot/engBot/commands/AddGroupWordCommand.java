package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
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
        log.info(chatId);
        Long senderId = update.getMessage().getFrom().getId();
        log.info(senderId);
        String cmd = update.getMessage().getText().replace("/add_group_word","");
        String groupName = null;
        String wordToLearn = null;
        String translate = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            groupName = cmdStructure[0].replace("@vocabengbot ", "").trim();
            wordToLearn = cmdStructure[1].trim();
            translate = cmdStructure[2].trim();
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group_word group name;word;translate");
            return;
        }
        String finalWordToLearn = wordToLearn;
        String finalTranslate = translate;
        String finalGroupName = groupName;
        groupService.findByGroupName(groupName).ifPresentOrElse(
                group -> {
                    ArrayList<Long> groupUsers = (ArrayList<Long>) groupService.getGroupUsers(group.getId());
                    ArrayList<Long> groupTeachers = (ArrayList<Long>) groupService.getGroupTeachers(group.getId());

                    if (groupUsers.contains(senderId)||groupTeachers.contains(senderId)) {
                        if (groupTeachers.contains(senderId)) sendBotMessageService.sendMessage(chatId, "You add words to your students.");
                        for (Long userId:groupUsers) {
                            vocabularyService.findByWordAndOwnerId(finalWordToLearn,userId).ifPresentOrElse(
                                    word ->{
                                        log.info("OLD WORD");
                                        if (userId.equals(senderId)) {
                                            sendBotMessageService.sendMessage(userId, "You have this word, but It was added to groupmates vocabulary.");
                                        }
                                    },
                                    ()->{
                                        log.info("NEW WORD");
                                        sendBotMessageService.sendMessage(userId, "New word has been added by your groupmate:" +
                                                "\nword -> " + finalWordToLearn +
                                                "\ntranslation -> " + finalTranslate +
                                                "\nrepeats -> " + VocabularyServiceImpl.REPEATS);
                                        Vocabulary vocabulary = new Vocabulary();
                                        vocabulary.setOwnerId(userId);
                                        vocabulary.setWord(finalWordToLearn);
                                        vocabulary.setWordTranslation(finalTranslate);
                                        vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                                        vocabulary.setActive(true);
                                        vocabularyService.addWord(vocabulary);
                                    }
                            );
                        }
                    } else {
                        sendBotMessageService.sendMessage(chatId, "Permissions denied. \nOnly group's members or teachers can add new word.");
                    }
                },
                () -> {
                    log.info("Can't find group");
                    sendBotMessageService.sendMessage(chatId, "Can't find group <b>" + finalGroupName + "</b>" +
                            "\nTry tu use command /show_my_group or /show_my_own_groups to find anyone.");
                }
        );

    }
}
