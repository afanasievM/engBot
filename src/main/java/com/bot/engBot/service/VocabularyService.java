package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Vocabulary;
import org.checkerframework.checker.nullness.Opt;

import java.util.List;
import java.util.Optional;

public interface VocabularyService {

    List<Vocabulary> retrieveAllUserWord(Long ownerId);

    List<Vocabulary> findAllByOwnerIdAndActiveTrue(Long ownerId);

    Optional<Vocabulary> findByWordAndOwnerId(String word, Long ownerId);

    Optional<Vocabulary> findByTranslationAndOwnerId(String translation, Long ownerId);

    Optional<Vocabulary> removeByWordAndOwnerId(String word, Long ownerId);

    void addWord(Vocabulary word);

    void wordsRepeatsDecrease(Vocabulary word);

    void wordsReset(Vocabulary word);

}
