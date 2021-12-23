package com.bot.engBot.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class BotUser {
    @Id
    @Column(name = "id",nullable = false)
    private Long id;


    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "user_language")
    private String user_language;

    @Column(name = "role")
    private String role;

    @Column(name = "join_at")
    private Timestamp join_at;

    @Column(name = "active")
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
