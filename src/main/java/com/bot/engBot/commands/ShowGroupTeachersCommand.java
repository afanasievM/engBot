package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.List;

public class ShowGroupTeachersCommand extends GroupCommand implements Command {
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(ShowGroupTeachersCommand.class);


    public ShowGroupTeachersCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService, GroupService groupService) {
        super(sendBotMessageService, groupService);
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        setSenderIdAndChatId(update);
        groupName = update.getMessage().getText().replace("/show_group_teachers", "").trim();
        if (groupName.isEmpty()) {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n" +
                    "/show_group_teachers group");
            return;
        }
        Group group = getGroup();
        if (group == null) {
            return;
        }
        List<Long> groupUsers = groupService.getGroupUsers(group.getId());
        List<Long> groupTeachers = groupService.getGroupTeachers(group.getId());
        if (!groupUsers.contains(senderId) && !groupTeachers.contains(senderId)) {
            sendBotMessageService.sendMessage(chatId, "Permissions denied. \n" +
                    "Only group's members or teachers can show info about group.");
            return;
        }
        sendGroupTeachers(groupTeachers);
    }

    private void sendGroupTeachers(List<Long> groupTeachers) {
        if (groupTeachers.isEmpty()){
            String message = String.format("Group <b>%s</b> hasn't teachers.",groupName);
            sendBotMessageService.sendMessage(chatId, message);
            return;
        }
        StringBuilder usersList = new StringBuilder();
        for (Long userId : groupTeachers) {
            botUserService.findByChatId(userId).ifPresentOrElse(
                    user -> {
                        usersList.append("@" + user.getUsername() + "\n");
                    },
                    () -> {
                        log.info("can't find user");
                    }
            );
        }
        sendBotMessageService.sendMessage(chatId, usersList.toString());
        sendBotMessageService.sendMessage(chatId, "Total:" + groupTeachers.size());
    }
}
