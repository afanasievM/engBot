package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bot.engBot.commands.CommandName.*;

public class HelpCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(HelpCommand.class);

    private String helpMessage;

    private final String FIRST_LINE = "✨<b>Available commands</b>✨\n\n";

    public HelpCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
        fillCommands();
    }


    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), helpMessage);
    }

    private void fillCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(START.getCommandName() + " - start working with me\n");
        commands.add(STOP.getCommandName() + " - stop working with me\n");
        commands.add(HELP.getCommandName() + " - show available commands\n");
        commands.add(ADD.getCommandName() + " - add new word to your vocabulary\n");
        commands.add(SHOW_MY_WORDS.getCommandName() + " - show all your words in vocabulary\n");
        commands.add(REMOVE_WORD.getCommandName() + " - remove word from your vocabulary\n");
        commands.add(REPLACE_WORD.getCommandName() + " - replace word in your vocabulary\n");
        commands.add(REPLACE_TRANSLATION.getCommandName() + " - replace word's translation in your vocabulary\n");
        commands.add(ADD_GROUP.getCommandName() + " - create new group\n");
        commands.add(ADD_GROUP_MEMBER.getCommandName() + " - add member to your group\n");
        commands.add(ADD_GROUP_TEACHER.getCommandName() + " - add teacher to your group\n");
        commands.add(ADD_GROUP_WORD.getCommandName() + " - add word to group members\n");
        commands.add(REMOVE_GROUP.getCommandName() + " - remove your group\n");
        commands.add(REMOVE_GROUP_MEMBER.getCommandName() + " - remove group member from your group\n");
        commands.add(SHOW_MY_UNSTUDIED_WORDS.getCommandName() + " - show your unstudied words in vocabulary\n");
        commands.add(SHOW_MY_GROUPS.getCommandName() + " - show groups where you are member\n");
        commands.add(SHOW_MY_OWN_GROUPS.getCommandName() + " - show groups where you are owner\n");
        commands.add(SHOW_GROUP_MEMBERS.getCommandName() + " - show group members\n");
        commands.add(SHOW_GROUP_TEACHERS.getCommandName() + " - show group teachers\n");
        commands.add(REMOVE_GROUP_TEACHER.getCommandName() + " - remove group teacher\n");
        commands.add(SHOW_MY_TEACH_GROUPS.getCommandName() + " - show groups where you are teacher\n");
        commands.add(REPEAT_ALL.getCommandName() + " - repeat all words from vocabulary\n");
        commands.add(REPEAT_WORD.getCommandName() + " - repeat word\n");
        Collections.sort(commands);

        helpMessage = FIRST_LINE + String.join("", commands);

    }
}
