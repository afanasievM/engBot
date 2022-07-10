package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class RemoveGroupCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(RemoveGroupCommand.class);
    private String groupName;
    private Long senderId;
    private Long chatId;


    public RemoveGroupCommand(SendBotMessageService sendBotMessageService, GroupService groupService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
        this.botUserService = botUserService;
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
