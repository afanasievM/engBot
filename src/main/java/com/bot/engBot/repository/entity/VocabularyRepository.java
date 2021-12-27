package com.bot.engBot.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByOwnerId(Long ownerId);
    Optional<Vocabulary> findByWordAndOwnerId(String word, Long ownerId);
    Optional<Vocabulary> findByWordTranslationAndOwnerId(String translation, Long ownerId);
    List<Vocabulary> findAllByOwnerIdAndActiveTrue(Long ownerId);
}