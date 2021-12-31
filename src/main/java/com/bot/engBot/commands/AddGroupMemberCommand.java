package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class AddGroupMemberCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(AddGroupMemberCommand.class);


    public AddGroupMemberCommand(SendBotMessageService sendBotMessageService, GroupService groupService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/add_group_member","");
        String groupName = null;
        String newMember = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            groupName = cmdStructure[0].replace("@vocabengbot ", "").trim();
            newMember = cmdStructure[1].trim();
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group_member group name;@username" +
                    "\nThis user should use @vocabengbot.(/start)");
            return;
        }
        if (!groupName.equals("") && !newMember.equals("")){
            String finalGroupName = groupName;
            String finalNewMember = newMember;
            groupService.findByGroupName(groupName).ifPresentOrElse(
                    group -> {
                        log.info("OLD Finded!");
                        if (group.getOwnerId() == chatId) {
                            botUserService.findByUsername(finalNewMember).ifPresentOrElse(
                                    user -> {
                                        sendBotMessageService.sendMessage(chatId, "User <b>" + user.getUsername() + "<b> successfuly added to group <b>" +
                                                group.getGroupName() + "</b>.");
                                    },
                                    () -> {
                                        sendBotMessageService.sendMessage(chatId, "Can't find user in bot's database. " +
                                                "\nThis user should use @vocabengbot.(/start)");
                                    }
                            );
                        } else {
                            sendBotMessageService.sendMessage(chatId, "Permissions denied. \nOnly group's admins or owner can add members to groups");
                        }
                        sendBotMessageService.sendMessage(chatId, "This group name exists.\nTry another name");
                    },
                    () -> {
                        log.info("Can't find group");
                        sendBotMessageService.sendMessage(chatId, "Can't find group <b>" + finalGroupName + "</b>" +
                                "\nTry tu use command /show_my_group or /show_my_own_groups to find anyone.");
                    }
            );
        } else {
            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/add_group_member group name;@username" +
                    "\nThis user should use @vocabengbot.(/start)");
        }



    }
}
