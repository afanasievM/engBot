package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.BotUserRepository;
import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.repository.entity.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Override
    public void save(Group group) {
        this.groupRepository.save(group);
    }

    @Override
    public List<Group> findAllByOwnerId(Long ownerId) {
        return groupRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public Optional<Group> findByGroupNameAndOwnerId(String groupName, Long ownerId) {
        return groupRepository.findByGroupNameAndOwnerId(groupName,ownerId);
    }

    @Override
    public Optional<Group> findByGroupName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }
}
