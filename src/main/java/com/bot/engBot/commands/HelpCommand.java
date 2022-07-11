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

                    + "%s - start working with me\n"                                //START
                    + "%s - stop working with me\n"                                 //STOP
                    + "%s - show available commands\n\n"                            //HELP
                    + "%s - add new word to your vocabulary\n"                      //ADD
                    + "%s - show all your words in vocabulary\n"                    //SHOW_MY_WORDS
                    + "%s - remove word from your vocabulary\n"                     //REMOVE_WORD
                    + "%s - replace word in your vocabulary\n"                      //REPLACE_WORD
                    + "%s - replace word's translation in your vocabulary\n"        //REPLACE_TRANSLATION
                    + "%s - create new group\n"                                     //ADD_GROUP
                    + "%s - add member to your group\n"                             //ADD_GROUP_MEMBER
                    + "%s - add teacher to your group\n"                            //ADD_GROUP_TEACHER
                    + "%s - add word to group members\n"                            //ADD_GROUP_WORD
                    + "%s - remove your group\n"                                    //REMOVE_GROUP
                    + "%s - remove group member from your group\n"                  //REMOVE_GROUP_MEMBER
                    + "%s - show your unstadied words in vocabulary\n"              //SHOW_MY_UNSTUDIED_WORDS
                    + "%s - show groups where you are member\n"                     //SHOW_MY_GROUPS
                    + "%s - show groups where you are owner\n"                      //SHOW_MY_OWN_GROUPS
                    + "%s - show group members\n"                                   //SHOW_GROUP_USERS
                    + "%s - show group teachers\n"                                  //SHOW_GROUP_TEACHERS
                    + "%s - remove group teacher\n"                                 //REMOVE_GROUP_TEACHER
                    + "%s - show groups where you are teacher\n"                    //SHOW_MY_TEACH_GROUPS
                    + "%s - repeat all words from vocabulary\n"                     //REPEAT_ALL
                    + "%s - repeat word",                                           //REPEAT_WORD
            START.getCommandName(),
            STOP.getCommandName(),
            HELP.getCommandName(),
            ADD.getCommandName(),
            SHOW_MY_WORDS.getCommandName(),
            REMOVE_WORD.getCommandName(),
            REPLACE_WORD.getCommandName(),
            REPLACE_TRANSLATION.getCommandName(),
            ADD_GROUP.getCommandName(),
            ADD_GROUP_MEMBER.getCommandName(),
            ADD_GROUP_TEACHER.getCommandName(),
            ADD_GROUP_WORD.getCommandName(),
            REMOVE_GROUP.getCommandName(),
            REMOVE_GROUP_MEMBER.getCommandName(),
            SHOW_MY_UNSTUDIED_WORDS.getCommandName(),
            SHOW_MY_GROUPS.getCommandName(),
            SHOW_MY_OWN_GROUPS.getCommandName(),
            SHOW_GROUP_MEMBERS.getCommandName(),
            SHOW_GROUP_TEACHERS.getCommandName(),
            REMOVE_GROUP_TEACHER.getCommandName(),
            SHOW_MY_TEACH_GROUPS.getCommandName(),
            REPEAT_ALL.getCommandName(),
            REMOVE_WORD.getCommandName()

    );

    public HelpCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
    }
    //TO DO
    // add array list with sorting by order
}
