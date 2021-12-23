package com.bot.engBot.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    List<BotUser> findAllByActiveTrue();

}