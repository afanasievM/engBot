package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;

import java.util.List;
import java.util.Optional;

public interface BotUserService {
    void save(BotUser botUser);

    List<BotUser> retrieveAllActiveUsers();

    Optional<BotUser> findByChatId(Long chatId);

    Optional<BotUser> findByUsername(String username);
}
