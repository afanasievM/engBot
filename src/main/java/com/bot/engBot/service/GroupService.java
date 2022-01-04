package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Group;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    void save(Group group);
    List<Group> findAllByOwnerId(Long ownerId);
    Optional<Group> findByGroupNameAndOwnerId(String groupName, Long ownerId);
    Optional<Group> findByGroupName(String groupName);
    void addGroupUser (Long groupId, Long userId);
}
