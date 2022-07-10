package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddGroupWordCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(AddGroupWordCommand.class);

    private String groupName;
    private String wordToLearn;
    private String translate;
    private Long chatId;
    private Long senderId;

    public AddGroupWordCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
        this.groupService = groupService;
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        log.info(chatId);
        senderId = update.getMessage().getFrom().getId();
        log.info(senderId);
        parse(update.getMessage().getText());
        if (wordToLearn == null || translate == null || groupName == null) {
            return;
        }
        Group group = getGroup();
        if (group == null) {
            return;
        }
        List<Long> groupUsers = groupService.getGroupUsers(group.getId());
        List<Long> groupTeachers = groupService.getGroupTeachers(group.getId());
        if (!groupUsers.contains(senderId) || !groupTeachers.contains(senderId)) {
            sendBotMessageService.sendMessage(chatId, "Permissions denied. \n" +
                    "Only group's members or teachers can add new word.");
            return;
        }
        sendBotMessageService.sendMessage(chatId, "You add word to your group.");
        addWordToGroupUsers(groupUsers);
    }

    private void addWordToGroupUsers(List<Long> groupUsers) {
        for (Long userId : groupUsers) {
            vocabularyService.findByWordAndOwnerId(wordToLearn, userId).ifPresentOrElse(
                    word -> {
                        log.info("OLD WORD");
                        if (userId.equals(senderId)) {
                            sendBotMessageService.sendMessage(chatId,
                                    "You have this word, but It was added to groupmates vocabulary.");
                        }
                    },
                    () -> {
                        log.info("NEW WORD");
                        String message = String.format("New word has been added by your groupmate:" +
                                        "\nword -> %s\ntranslation -> %s\nrepeats -> %d",
                                        wordToLearn, translate, VocabularyServiceImpl.REPEATS);
                        sendBotMessageService.sendMessage(userId, message);
                        Vocabulary vocabulary = new Vocabulary();
                        vocabulary.setOwnerId(userId);
                        vocabulary.setWord(wordToLearn);
                        vocabulary.setWordTranslation(translate);
                        vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                        vocabulary.setActive(true);
                        vocabularyService.addWord(vocabulary);
                    }
            );
        }
    }

    private void parse(String updateText) {
        String cmd = updateText.replace("/add_group_word", "").trim();
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            groupName = cmdStructure[0].replace("@vocabengbot ", "").trim();
            wordToLearn = cmdStructure[1].trim();
            translate = cmdStructure[2].trim();
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n" +
                    "/add_group_word group name;word;translate");
        }
    }

    private Group getGroup() {
        Optional<Group> optionalGroup = groupService.findByGroupName(groupName);
        if (!optionalGroup.isPresent()) {
            log.info("Can't find group");
            String message = String.format("Can't find group <b>%s</b>\n" +
                    "Try tu use command /show_my_group or /show_my_own_groups to find anyone.", groupName);
            sendBotMessageService.sendMessage(chatId, message);
            return null;
        }
        return optionalGroup.get();
    }
}
