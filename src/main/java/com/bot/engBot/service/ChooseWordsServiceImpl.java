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
public class ChooseWordsServiceImpl implements ChooseWordsService {
    final private BotUserService botUserService;
    final private VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(ChooseWordsServiceImpl.class);
    final private int WORDS_COUNT;
    public static ConcurrentHashMap<Long, List<Vocabulary>> listToSend = new ConcurrentHashMap<>();

    @Autowired
    public ChooseWordsServiceImpl(BotUserService botUserService,
                                  VocabularyService vocabularyService) {
        this.botUserService = botUserService;
        this.vocabularyService = vocabularyService;
        this.WORDS_COUNT = 5;
    }

    @Override
    public void chooseWordsToTest() {
        List<BotUser> users = botUserService.retrieveAllActiveUsers();
        listToSend.clear();
        for (BotUser user : users) {
            List<Vocabulary> wordsToTest = chooseWordsUser(user);
            if (wordsToTest.isEmpty()){
                continue;
            }
            log.info(user.getId().toString() + wordsToTest.toString());
            listToSend.put(user.getId(), wordsToTest);
        }
    }

    private List<Vocabulary> chooseWordsUser(BotUser user) {
        List<Vocabulary> userWords = vocabularyService.findAllByOwnerIdAndActiveTrue(user.getId());
        if (userWords.size() < WORDS_COUNT) {
            return userWords;
        }
        List<Vocabulary> wordsToTest = new ArrayList<>();
        for (int i = 0; i < WORDS_COUNT; i++) {
            Random random = new Random();
            Integer localWordId = random.nextInt(userWords.size());
            if (!wordsToTest.contains(userWords.get(localWordId)))
                wordsToTest.add(userWords.get(localWordId));
            else i--;
        }
        return wordsToTest;
    }
}
