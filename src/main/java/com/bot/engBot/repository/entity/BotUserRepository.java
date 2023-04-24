package com.bot.engBot.repository.entity;

import com.bot.engBot.bot.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.BitSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    List<BotUser> findAllByActiveTrue();
    Optional<BotUser> findByUsername(String username);
}