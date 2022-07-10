package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.print.DocFlavor;
import java.util.Arrays;
import java.util.Optional;

public class AddGroupMemberCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(AddGroupMemberCommand.class);
    private String groupName;
    private String newMember;
    private Long chatId;
    private Long senderId;


    public AddGroupMemberCommand(SendBotMessageService sendBotMessageService, GroupService groupService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        senderId = update.getMessage().getFrom().getId();
        parse(update.getMessage().getText());
        if (groupName == null || newMember == null) {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n" +
                    "/add_group_member group name;@username\n" +
                    "This user should use @vocabengbot.(/start)");
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
        addUserToGroup(group, user);
    }

    private void parse(String updateText) {
        String cmd = updateText.replace("/add_group_member", "");
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            groupName = cmdStructure[0].replace("@vocabengbot ", "").trim();
            newMember = cmdStructure[1].trim();
            if (newMember.startsWith("@")) newMember = newMember.replaceFirst("@", "");
        } catch (Exception e) {
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group_member group name;@username" +
                    "\nThis user should use @vocabengbot.(/start)");
            return;
        }
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

    private boolean isValidGroup(Group group) {
        if (group == null) {
            return false;
        }
        log.info("OLD group Finded!");
        if (!group.getOwnerId().equals(senderId)) {
            sendBotMessageService.sendMessage(chatId, "Permissions denied. \n" +
                    "Only group's admins or owner can add members to groups");
            return false;
        }
        return true;
    }

    private BotUser getUser() {
        Optional<BotUser> optionalUser = botUserService.findByUsername(newMember);
        if (!optionalUser.isPresent()) {
            sendBotMessageService.sendMessage(chatId,
                    "Can't find user in bot's database.\n" +
                            "This user should use @vocabengbot.(/start)");
            return null;
        }
        return optionalUser.get();
    }

    private void addUserToGroup(Group group, BotUser user) {
        String message;
        try {
            groupService.addGroupUser(group.getId(), user.getId());
            message = String.format("User <b>%s</b> successfuly added to group <b>%s</b>.",
                    user.getUsername(), group.getGroupName());
            sendBotMessageService.sendMessage(chatId, message);
            message = String.format("You have been added to group <b>%s</b> by @%s.",
                    group.getGroupName(), botUserService.findByChatId(senderId).get().getUsername());
            sendBotMessageService.sendMessage(user.getId(), message);
        } catch (DataIntegrityViolationException e) {
            message = String.format("User <b>%s</b> is already exist in group <b>%s</b>.",
                    user.getUsername(), group.getGroupName());
            sendBotMessageService.sendMessage(chatId, message);
            log.info(e);
        }
    }
}
