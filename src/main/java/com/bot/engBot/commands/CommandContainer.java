package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.google.common.collect.ImmutableMap;

import static com.bot.engBot.commands.CommandName.*;

public class CommandContainer {
    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, BotUserService botUserService, VocabularyService vocabularyService) {

        commandMap = ImmutableMap.<String,Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, botUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, botUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService, botUserService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, botUserService))
                .put(ADD.getCommandName(), new AddCommand(sendBotMessageService, vocabularyService))
                .put(SHOW_MY_WORDS.getCommandName(), new ShowMyWordsCommand(sendBotMessageService, vocabularyService))
                .put(REMOVE_WORD.getCommandName(), new RemoveWordCommand(sendBotMessageService, vocabularyService))
                .put(REPLACE_WORD.getCommandName(), new ReplaceWordCommand(sendBotMessageService, vocabularyService))
                .put(REPLACE_TRANSLATION.getCommandName(), new ReplaceTranslationCommand(sendBotMessageService, vocabularyService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
