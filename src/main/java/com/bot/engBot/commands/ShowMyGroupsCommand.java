package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ShowMyGroupsCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(ShowMyGroupsCommand.class);


    public ShowMyGroupsCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Long> groupList = new ArrayList<>();
        groupList.addAll(groupService.getUserGroupsId(update.getMessage().getFrom().getId()));
        StringBuilder groupsListStr = new StringBuilder();
        for (Long groupId:groupList) {
            groupService.findById(groupId).ifPresentOrElse(
                    group -> {
                        groupsListStr.append(group.getGroupName() + "\n");
                    },
                    () -> {
                        log.info("Can't find group with id:" + groupId);
                    }
            );

        }
        sendBotMessageService.sendMessage(chatId,groupsListStr.toString());
    }
}
