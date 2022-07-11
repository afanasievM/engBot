package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;


public class RemoveGroupCommand extends GroupCommand implements Command {

    final private Logger log = Logger.getLogger(RemoveGroupCommand.class);


    public RemoveGroupCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        super(sendBotMessageService, groupService);
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        groupName = update.getMessage().getText().replace("/remove_group", "").trim();
        if (groupName.isEmpty() || groupName == null) {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/remove_group group name");
            return;
        }
        Group group = getGroup();
        if (group == null) {
            return;
        }
        removeUserGroup(group);

    }

    private void removeUserGroup(Group group) {
        if (!senderId.equals(group.getOwnerId())) {
            sendBotMessageService.sendMessage(chatId, "Permissions denied.\n" +
                    "Only group's owner can remove groups.");
            return;
        }
        sendBotMessageService.sendMessage(chatId, "You removed group <b>" + group.getGroupName() + "</b>");
        groupService.removeGroup(group.getId());
    }


}
