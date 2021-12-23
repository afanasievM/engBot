package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface BotUserService {
    void save(BotUser botUser);

    List<BotUser> retrieveAllActiveUsers();

    Optional<BotUser> findByChatId(Long chatId);
}
