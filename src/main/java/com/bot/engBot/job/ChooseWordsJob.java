package com.bot.engBot.job;

import com.bot.engBot.service.ChooseWordsService;
import com.bot.engBot.service.SendBotMessageServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ChooseWordsJob {
    private final ChooseWordsService chooseWordsService;
    final static Logger log = Logger.getLogger(SendBotMessageServiceImpl.class);


    @Autowired
    public ChooseWordsJob(ChooseWordsService chooseWordsService){
        this.chooseWordsService = chooseWordsService;
    }

    @Scheduled(fixedRate = 60000*5)
//    @Scheduled(fixedRate = 10000)
    public void chooseWords(){
        LocalDateTime start = LocalDateTime.now();

        log.info("Choose words job started.");

        chooseWordsService.chooseWordsToTest();

        LocalDateTime end = LocalDateTime.now();

        log.info("Choose words job finished. Took seconds: " +
                (end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)));
    }

}
