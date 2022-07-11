package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class RemoveGroupTeacherCommand extends GroupUserCommand implements Command {

    final private Logger log = Logger.getLogger(RemoveGroupTeacherCommand.class);


    public RemoveGroupTeacherCommand(SendBotMessageService sendBotMessageService, GroupService groupService, BotUserService botUserService) {
        super(sendBotMessageService, groupService, botUserService);
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        parse(update.getMessage().getText());
        if (groupName == null || user == null) {
            return;
        }
        Group group = getGroup();
        if (!isValidGroup(group)) {
            return;
        }
        BotUser user = getUser();
        if (user == null) {
            return;
        }
        removeTeacherFromGroup(group, user);
    }

    private void removeTeacherFromGroup(Group group, BotUser user) {
        String message;
        try {
            groupService.removeGroupTeacher(group.getId(), user.getId());
            message = String.format("Teacher <b>%s</b> successfully removed from group <b>%s</b>.",
                    user.getUsername(), group.getGroupName());
            sendBotMessageService.sendMessage(chatId, message);
        } catch (DataIntegrityViolationException e) {
            message = String.format("Can't find teacher <b>%s</b> in group <b>%s</b>.",
                    user.getUsername(), group.getGroupName());
            sendBotMessageService.sendMessage(chatId, message);
            log.info(e);
        }
    }

    private void parse(String updateText) {
        String cmd = updateText.replace("/remove_group_member", "");
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            groupName = cmdStructure[0].replace("@vocabengbot ", "").trim();
            user = cmdStructure[1].trim();
            if (user.startsWith("@")) {
                user = user.replaceFirst("@", "");
            }
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n" +
                    "/remove_group_member group name;@username\n" +
                    "This user should use be in a your group.");
        }
    }

    private boolean isValidGroup(Group group) {
        if (group == null) {
            return false;
        }
        log.info("OLD group Finded!");
        if (!group.getOwnerId().equals(senderId)) {
            sendBotMessageService.sendMessage(chatId, "Permissions denied. \n" +
                    "Only group's admins or owner can remove teachers from groups");
            return false;
        }
        return true;
    }
}
