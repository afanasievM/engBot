package com.bot.engBot.repository.entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @Column(name = "word_id",nullable = false)
    private Long word_id;

    @Column(name = "owner_id",nullable = false)
    private Long owner_id;

    @Column(name = "word")
    private String word;

    @Column(name = "word_translation")
    private String word_translation;


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
}
//    CREATE TABLE vocabulary(
//        word_id INT NOT NULL AUTO_INCREMENT,
//        owner_id INT NOT NULL,
//        word VARCHAR(100),
//    word_translation VARCHAR(100),
//    FOREIGN KEY(owner_id) REFERENCES users(id),
//        PRIMARY KEY (word_id),
//        UNIQUE(owner_id,word)
//        );