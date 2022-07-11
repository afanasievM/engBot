package com.bot.engBot.commands;

import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ShowMyTeachGroupsCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    final private Logger log = Logger.getLogger(ShowMyTeachGroupsCommand.class);


    public ShowMyTeachGroupsCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Long> groupList = groupService.getTeacherGroupsId(update.getMessage().getFrom().getId());
        if (groupList.isEmpty()) {
            sendBotMessageService.sendMessage(chatId,"You have not teach groups.");
        }
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
