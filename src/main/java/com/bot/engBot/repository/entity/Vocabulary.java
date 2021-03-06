package com.bot.engBot.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id", nullable = false, insertable = true)
    private Long wordId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "word")
    private String word;

    @Column(name = "word_translation")
    private String wordTranslation;

    @Column(name = "repeats")
    private int repeats;

    @Column(name = "active")
    private boolean active;
}
