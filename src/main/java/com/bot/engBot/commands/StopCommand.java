package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(StopCommand.class);
    public final static String STOP_MESSAGE = "Your subscription is deactivated";

    public StopCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;

    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), STOP_MESSAGE);
        botUserService.findByChatId(update.getMessage().getFrom().getId()).ifPresent(it -> {
            log.info(it);
            it.setActive(false);
            botUserService.save(it);
            String message = String.format("User %s id: %d stopped bot",
                    update.getMessage().getChat().getUserName(), update.getMessage().getChatId());
            log.info(message);
        });
    }
}
