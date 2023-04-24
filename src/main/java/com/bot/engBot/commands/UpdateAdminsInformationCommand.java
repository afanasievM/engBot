package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUserRepository;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class UpdateAdminsInformationCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final BotUserService botUserService;
    final private Logger log = Logger.getLogger(UpdateAdminsInformationCommand.class);
    @Value("${project.version}")
    private String version;
    @Value("${project.artifactId}")
    private String artifactId;


    public UpdateAdminsInformationCommand(SendBotMessageService sendBotMessageService, BotUserService botUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(Update update) {
        String message = "Deployed " + artifactId + "@" + version;
        botUserService.retrieveAllActiveUsers().stream()
                .filter(user -> user.getRole().equals("admin"))
                .forEach(user -> sendBotMessageService.sendMessage(user.getId(), message));
    }
}
