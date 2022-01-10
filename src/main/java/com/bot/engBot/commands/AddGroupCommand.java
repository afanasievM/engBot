package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class AddGroupCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(AddGroupCommand.class);


    public AddGroupCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;

    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long senderId = update.getMessage().getFrom().getId();
        String cmd = update.getMessage().getText().replace("/add_group","");
        String groupName = cmd.trim();
        if (!groupName.equals("")){
            groupService.findByGroupName(groupName).ifPresentOrElse(
                    group -> {
                        log.info("OLD Group");
                        sendBotMessageService.sendMessage(chatId, "This group name exists.\nTry another name.");
                    },
                    () -> {
                        log.info("NEW Group");
                        sendBotMessageService.sendMessage(chatId, "You add new group:" + groupName
                        + "\nYou can add members by command\n/add_group_member group name;@username\n" +
                                "This user should use @vocabengbot.(/start)");
                        Group group = new Group();
                        group.setGroupName(groupName);
                        group.setOwnerId(senderId);
                        groupService.save(group);
                        Group newGroup = groupService.findByGroupName(groupName).get();
                        groupService.addGroupUser(newGroup.getId(),senderId);
                    }
            );
        } else {

            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group group name");
        }



    }
}
