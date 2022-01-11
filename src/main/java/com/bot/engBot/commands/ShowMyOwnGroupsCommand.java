package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ShowMyOwnGroupsCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(ShowMyOwnGroupsCommand.class);


    public ShowMyOwnGroupsCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Group> groupList = new ArrayList<>();
        groupList.addAll(groupService.findAllByOwnerId(update.getMessage().getFrom().getId()));
        StringBuilder groupsListStr = new StringBuilder();
        for (Group group:groupList) {
            groupsListStr.append(group.getGroupName() + "\n");
        }
        sendBotMessageService.sendMessage(chatId,groupsListStr.toString());
    }
}
