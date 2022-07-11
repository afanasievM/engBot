package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.GroupService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.google.common.collect.ImmutableMap;

import static com.bot.engBot.commands.CommandName.*;

public class CommandContainer {
    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;
    private final Command noCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, BotUserService botUserService, VocabularyService vocabularyService, GroupService groupService) {

        commandMap = ImmutableMap.<String,Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, botUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, botUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService, botUserService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, botUserService))
                .put(ADD.getCommandName(), new AddWordCommand(sendBotMessageService, vocabularyService))
                .put(SHOW_MY_WORDS.getCommandName(), new ShowMyWordsCommand(sendBotMessageService, vocabularyService))
                .put(REMOVE_WORD.getCommandName(), new RemoveWordCommand(sendBotMessageService, vocabularyService))
                .put(REPLACE_WORD.getCommandName(), new ReplaceWordCommand(sendBotMessageService, vocabularyService))
                .put(REPLACE_TRANSLATION.getCommandName(), new ReplaceTranslationCommand(sendBotMessageService, vocabularyService))
                .put(GET_MY_ID.getCommandName(), new GetIDCommand(sendBotMessageService))
                .put(ADD_GROUP.getCommandName(), new AddGroupCommand(sendBotMessageService, groupService))
                .put(ADD_GROUP_MEMBER.getCommandName(), new AddGroupMemberCommand(sendBotMessageService, groupService, botUserService))
                .put(REMOVE_GROUP_MEMBER.getCommandName(), new RemoveGroupMemberCommand(sendBotMessageService, groupService, botUserService))
                .put(REMOVE_GROUP.getCommandName(), new RemoveGroupCommand(sendBotMessageService, groupService))
                .put(ADD_GROUP_WORD.getCommandName(), new AddGroupWordCommand(sendBotMessageService,vocabularyService, groupService))
                .put(ADD_GROUP_TEACHER.getCommandName(), new AddGroupTeacherCommand(sendBotMessageService, groupService, botUserService))
                .put(SHOW_MY_UNSTUDIED_WORDS.getCommandName(), new ShowMyUnstudiedWordsCommand(sendBotMessageService, vocabularyService))
                .put(SHOW_MY_GROUPS.getCommandName(), new ShowMyGroupsCommand(sendBotMessageService, groupService))
                .put(SHOW_MY_OWN_GROUPS.getCommandName(), new ShowMyOwnGroupsCommand(sendBotMessageService, groupService))
                .put(SHOW_GROUP_MEMBERS.getCommandName(), new ShowGroupMemberCommand(sendBotMessageService,botUserService, groupService))
                .put(SHOW_GROUP_TEACHERS.getCommandName(), new ShowGroupTeachersCommand(sendBotMessageService,botUserService, groupService))
                .put(REMOVE_GROUP_TEACHER.getCommandName(), new RemoveGroupTeacherCommand(sendBotMessageService, groupService, botUserService))
                .put(SHOW_MY_TEACH_GROUPS.getCommandName(), new ShowMyTeachGroupsCommand(sendBotMessageService, groupService))
                .put(REPEAT_ALL.getCommandName(), new RepeatAllWordsCommand(sendBotMessageService, vocabularyService))
                .put(REMOVE_WORD.getCommandName(), new RepeatWordCommand(sendBotMessageService, vocabularyService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
        noCommand = new NoCommand(sendBotMessageService);

    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
    public Command noCommand(){
        return noCommand;
    }
}
