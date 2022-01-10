package com.bot.engBot.commands;

import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.SendBotMessageServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command{
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
        botUserService.findByChatId(update.getMessage().getFrom().getId())
                .ifPresent(it -> {
                    log.info(it);
                    it.setActive(false);
                    botUserService.save(it);
                    log.info("User " + update.getMessage().getChat().getUserName() + " id:" + update.getMessage().getChatId() + " stopped bot" );
                });
    }
}
