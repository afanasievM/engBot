package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.BotUserRepository;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.repository.entity.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VocabularyServiceImpl implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;

    @Autowired
    public VocabularyServiceImpl(VocabularyRepository vocabularyRepository) {
        this.vocabularyRepository = vocabularyRepository;
    }


    @Override
    public List<Vocabulary> retrieveAllUserWord(Long ownerId) {
        return vocabularyRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public Optional<Vocabulary> findByWordAndOwnerId(String word, Long ownerId) {
        return vocabularyRepository.findByWordAndOwnerId(word, ownerId);
    }

    @Override
    public Optional<Vocabulary> removeByWordAndOwnerId(String word, Long ownerId) {
        return Optional.empty();
    }

    @Override
    public void addWord(Vocabulary word) {
        vocabularyRepository.save(word);
    }
}
