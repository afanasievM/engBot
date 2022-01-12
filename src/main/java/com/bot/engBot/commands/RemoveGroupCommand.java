package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RemoveGroupCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(RemoveGroupCommand.class);


    public RemoveGroupCommand(SendBotMessageService sendBotMessageService, GroupService groupService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupService = groupService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long senderId = update.getMessage().getFrom().getId();
        String cmd = update.getMessage().getText().replace("/remove_group","");
        String groupName = cmd.trim();
        if (!groupName.equals("")){
            groupService.findByGroupName(groupName).ifPresentOrElse(
                    group -> {
                        log.info("OLD Group");
                        if (senderId.equals(group.getOwnerId())) {
                            sendBotMessageService.sendMessage(chatId, "You removed group <b>" + group.getGroupName() + "</b>");
                            groupService.removeGroup(group.getId());
                        } else {
                            sendBotMessageService.sendMessage(chatId, "Permissions denied. \nOnly group's owner can remove groups.");
                        }
                    },
                    () -> {
                        log.info("Can't find Group");
                        sendBotMessageService.sendMessage(chatId, "Can't find group:" + groupName
                        + "\nYou can see all your groups by command\n/show_my_groups");
                    }
            );
        } else {

            sendBotMessageService.sendMessage(chatId, "Please use correct form: \n/remove_group group name");
        }



    }
}
