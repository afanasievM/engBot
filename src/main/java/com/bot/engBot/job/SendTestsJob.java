package com.bot.engBot.job;

import com.bot.engBot.service.SendBotMessageServiceImpl;
import com.bot.engBot.service.SendTestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class SendTestsJob {
    private final SendTestService sendTestService;
    final static Logger log = Logger.getLogger(SendBotMessageServiceImpl.class);


    @Autowired
    public SendTestsJob(SendTestService sendTestService) {
        this.sendTestService = sendTestService;
    }

    @Scheduled(cron = "0 0 8-21 * * *")
//    @Scheduled(fixedDelay = 30000, initialDelay = 20000)
    public void chooseWords() {
        LocalDateTime start = LocalDateTime.now();

        log.info("Send tests job started.");

        sendTestService.sendWordsToTest();

        LocalDateTime end = LocalDateTime.now();

        log.info("Send tests job finished. Took seconds: " +
                (end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)));
    }

}
