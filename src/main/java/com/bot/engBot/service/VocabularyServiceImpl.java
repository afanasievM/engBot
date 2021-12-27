package com.bot.engBot.service;

import com.bot.engBot.commands.AddCommand;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.repository.entity.VocabularyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VocabularyServiceImpl implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;
    final private Logger log = Logger.getLogger(VocabularyServiceImpl.class);

    public final static int REPEATS = 5;

    @Autowired
    public VocabularyServiceImpl(VocabularyRepository vocabularyRepository) {
        this.vocabularyRepository = vocabularyRepository;

    }


    @Override
    public List<Vocabulary> retrieveAllUserWord(Long ownerId) {
        return vocabularyRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<Vocabulary> findAllByOwnerIdAndActiveTrue(Long ownerId){return vocabularyRepository.findAllByOwnerIdAndActiveTrue(ownerId);}

    @Override
    public Optional<Vocabulary> findByWordAndOwnerId(String word, Long ownerId) {
        return vocabularyRepository.findByWordAndOwnerId(word, ownerId);
    }
    @Override
    public Optional<Vocabulary> findByTranslationAndOwnerId(String translation, Long ownerId) {
        return vocabularyRepository.findByWordTranslationAndOwnerId(translation, ownerId);
    }

    @Override
    public Optional<Vocabulary> removeByWordAndOwnerId(String word, Long ownerId) {
        return Optional.empty();
    }

    @Override
    public void addWord(Vocabulary word) {
        vocabularyRepository.save(word);
    }

    @Override
    public void wordsRepeatsDecrease(Vocabulary word) {
        int currentRepeats = word.getRepeats();
        log.info(currentRepeats);
        currentRepeats = currentRepeats - 1;
        log.info(currentRepeats);
        word.setRepeats(currentRepeats);
        if (currentRepeats < 1) {

            word.setRepeats(0);
            word.setActive(false);
        }
        log.info("Word -> " + word.getWord() + "repeats -> " + word.getRepeats());
        vocabularyRepository.save(word);
    }

    @Override
    public void wordsReset(Vocabulary word) {
        word.setActive(true);
        word.setRepeats(REPEATS);
        vocabularyRepository.save(word);
    }


}
