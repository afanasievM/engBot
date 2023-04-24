package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.BotUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BotUserServiceImpl implements BotUserService {
    private final BotUserRepository botUserRepository;

    @Autowired
    public BotUserServiceImpl(BotUserRepository botUserRepository) {
        this.botUserRepository = botUserRepository;
    }

    @Override
    public void save(BotUser BotUser) {
        botUserRepository.save(BotUser);
    }

    @Override
    public List<BotUser> retrieveAllActiveUsers() {
        return botUserRepository.findAllByActiveTrue();
    }

    @Override
    public Optional<BotUser> findByChatId(Long chatId) {
        return botUserRepository.findById(chatId);
    }

    @Override
    public Optional<BotUser> findByUsername(String username) {
        return botUserRepository.findByUsername(username);
    }


}
