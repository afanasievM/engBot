package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;

import java.util.Optional;

public class GroupUserCommand extends GroupCommand {
    protected BotUserService botUserService;
    protected String user;
    public GroupUserCommand(SendBotMessageService sendBotMessageService, GroupService groupService,
                            BotUserService botUserService) {
        super(sendBotMessageService, groupService);
        this.botUserService = botUserService;
    }

    protected BotUser getUser() {
        Optional<BotUser> optionalUser = botUserService.findByUsername(user);
        if (!optionalUser.isPresent()) {
            sendBotMessageService.sendMessage(chatId,
                    "Can't find user in bot's database.\n" +
                            "This user should use @vocabengbot.(/start)");
            return null;
        }
        return optionalUser.get();
    }
}
