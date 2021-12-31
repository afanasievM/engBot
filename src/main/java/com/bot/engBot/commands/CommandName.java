package com.bot.engBot.commands;

public enum CommandName {
    START("/start"),
    STOP("/stop"),

    ADD("/add"),
    ADD_GROUP("/add_group"),
    ADD_GROUP_WORD("/add_group_word"),
    ADD_GROUP_TEACHER("/add_group_teacher"),
    ADD_GROUP_USER("/add_group_user"),

    SHOW_MY_WORDS("/show_my_words"),
    SHOW_MY_GROUPS("/show_my_groups"),
    SHOW_MY_OWN_GROUPS("/show_my_own_groups"),
    SHOW_UNEXPLORED_WORDS("/show_unexplored_words"),
    SHOW_GROUP_ADMINS("/show_group_admins"),
    SHOW_GROUP_USERS("/show_group_users"),
    SHOW_GROUP_TEACHERS("/show_group_teachers"),

    REMOVE_GROUP_USER("/remove_group_user"),
    REMOVE_GROUP_ADMIN("/remove_group_admin"),
    REMOVE_GROUP_TEACHER("/remove_group_teacher"),
    REMOVE_GROUP("/remove_group"),
    REMOVE_WORD("/remove_word"),
    REPLACE_WORD("/replace_word"),
    REPLACE_TRANSLATION("/replace_translation"),

    SET_GROUP_TEACHER("/set_group_teacher"),
    SET_GROUP_ADMIN("/set_group_admin"),

    GET_MY_ID("/get_my_id"),
    HELP("/help"),
    STAT("/stat")
    ;

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
