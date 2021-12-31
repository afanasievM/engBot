package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Vocabulary;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChooseWordsServiceImpl implements ChooseWordsService{
    final private BotUserService botUserService;
    final private VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ChooseWordsServiceImpl.class);
    final private int WORDS_COUNT;
    public static ConcurrentHashMap<Long, ArrayList<Vocabulary>> listToSend = new ConcurrentHashMap<>();
    @Autowired
    public ChooseWordsServiceImpl(BotUserService botUserService,
                                  VocabularyService vocabularyService) {
        this.botUserService = botUserService;
        this.vocabularyService = vocabularyService;
        this.WORDS_COUNT = 5;
    }

    @Override
    public void chooseWordsToTest() {

        List<BotUser> users = new ArrayList<>();
        users.addAll(botUserService.retrieveAllActiveUsers());
        listToSend.clear();
        for (BotUser user:users) {
            List<Vocabulary> userWords = new ArrayList<>();
            List<Vocabulary> wordsToTest = new ArrayList<>();
            userWords.addAll(vocabularyService.findAllByOwnerIdAndActiveTrue(user.getId()));
            if (userWords.size() == 0) continue;
            if (userWords.size() < WORDS_COUNT){
                wordsToTest.addAll(userWords);
            } else {
                for (int i = 0; i < WORDS_COUNT; i++) {
                    Random random = new Random();
                    Integer localWordId = random.nextInt(userWords.size());
                    if (!wordsToTest.contains(userWords.get(localWordId)))
                        wordsToTest.add(userWords.get(localWordId));
                    else i--;
                }
            }
            log.info(user.getId().toString() + wordsToTest.toString());
            listToSend.put(user.getId(), (ArrayList<Vocabulary>) wordsToTest);
        }
    }
}
