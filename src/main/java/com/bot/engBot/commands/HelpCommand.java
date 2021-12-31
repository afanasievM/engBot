package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import static com.bot.engBot.commands.CommandName.*;

public class HelpCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(HelpCommand.class);
    public static final String HELP_MESSAGE = String.format("✨<b>Available commands</b>✨\n\n"

                    + "%s - start working with me\n"
                    + "%s - stop working with me\n"
                    + "%s - show available commands\n\n"
                    + "%s - add new word to your vocabulary\n"
                    + "%s - show all your words in vocabulary\n"
                    + "%s - remove word from your vocabulary\n"
                    + "%s - replace word in your vocabulary\n"
                    + "%s - replace word's translation in your vocabulary",
            START.getCommandName(),
            STOP.getCommandName(),
            HELP.getCommandName(),
            ADD.getCommandName(),
            SHOW_MY_WORDS.getCommandName(),
            REMOVE_WORD.getCommandName(),
            REPLACE_WORD.getCommandName(),
            REPLACE_TRANSLATION.getCommandName()
    );

    public HelpCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
    }
}
