package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public class ShowGroupTeachersCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(ShowGroupTeachersCommand.class);


    public ShowGroupTeachersCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService, GroupService groupService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long senderId = update.getMessage().getFrom().getId();
        String groupName = update.getMessage().getText().replace("/show_group_users","").trim();
        if (groupName.equals("")) return;

        groupService.findByGroupName(groupName).ifPresentOrElse(
                group -> {
                    ArrayList<Long> groupUsers = (ArrayList<Long>) groupService.getGroupUsers(group.getId());
                    ArrayList<Long> groupTeachers = (ArrayList<Long>) groupService.getGroupTeachers(group.getId());
                    StringBuilder usersList = new StringBuilder();
                    if (groupUsers.contains(senderId)||groupTeachers.contains(senderId)) {
                        for (Long userId:groupTeachers) {
                            botUserService.findByChatId(userId).ifPresentOrElse(
                                    user ->{
                                        usersList.append("@" + user.getUsername() + "\n");
                                    },
                                    ()->{
                                        log.info("can't find user");
                                    }
                            );
                        }
                        sendBotMessageService.sendMessage(chatId, usersList.toString());
                        sendBotMessageService.sendMessage(chatId, "Total:" + groupUsers.size());
                    } else {
                        sendBotMessageService.sendMessage(chatId, "Permissions denied. \nOnly group's members or teachers can show info about group.");
                    }
                },
                () -> {
                    log.info("Can't find group");
                    sendBotMessageService.sendMessage(chatId, "Can't find group <b>" + groupName + "</b>" +
                            "\nTry tu use command /show_my_group or /show_my_own_groups to find anyone.");
                }
        );

    }
}
