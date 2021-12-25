package com.bot.engBot.repository.entity;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id",nullable = false, insertable = true)
    private Long word_id;

    @Column(name = "owner_id",nullable = false)
    private Long ownerId;

    @Column(name = "word")
    private String word;

    @Column(name = "word_translation")
    private String word_translation;

    @Column(name = "repeats")
    private int repeats;
}
