package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;

import java.util.Optional;

public class GroupCommand {

    protected final SendBotMessageService sendBotMessageService;

    protected final GroupService groupService;

    protected String groupName;

    protected Long senderId;

    protected Long chatId;
    final private Logger log = Logger.getLogger(GroupCommand.class);

    public GroupCommand(SendBotMessageService sendBotMessageService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;

    }

    protected Group getGroup() {
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
