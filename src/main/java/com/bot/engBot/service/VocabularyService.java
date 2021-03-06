package com.bot.engBot.service;

import com.bot.engBot.repository.entity.Vocabulary;

import java.util.List;
import java.util.Optional;

public interface VocabularyService {

    List<Vocabulary> retrieveAllUserWord(Long ownerId);

    List<Vocabulary> findAllByOwnerIdAndActiveTrue(Long ownerId);
    List<Vocabulary> findAllByOwnerIdAndActiveFalse(Long ownerId);

    Optional<Vocabulary> findByWordAndOwnerId(String word, Long ownerId);

    Optional<Vocabulary> findByTranslationAndOwnerId(String translation, Long ownerId);

    Optional<Vocabulary> removeByWordAndOwnerId(String word, Long ownerId);

    void addWord(Vocabulary word);

    void decreaseWordRepeats(Vocabulary word);

    void resetWord(Vocabulary word);

    void removeWord(Vocabulary word);

    void save(Vocabulary word);
}
