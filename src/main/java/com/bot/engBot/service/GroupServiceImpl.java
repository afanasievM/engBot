package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.BotUserRepository;
import com.bot.engBot.repository.entity.Group;
import com.bot.engBot.repository.entity.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public Optional<Group> findById(Long groupId) {
        return groupRepository.findById(groupId);
    }

    @Override
    public void addGroupUser (Long groupId, Long userId){
        groupRepository.addGroupUser(groupId, userId);
    }

    @Override
    public void addGroupTeacher(Long groupId, Long userId) {
        groupRepository.addGroupTeacher(groupId,userId);
    }

    @Override
    public void addGroupAdmin(Long groupId, Long userId) {
        groupRepository.addGroupAdmin(groupId,userId);
    }

    @Override
    public void removeGroupUser(Long groupId, Long userId) {
        groupRepository.removeGroupUser(groupId,userId);
    }

    @Override
    public void removeGroupTeacher(Long groupId, Long userId) {
        groupRepository.removeGroupTeacher(groupId,userId);
    }

    @Override
    public void removeGroupAdmin(Long groupId, Long userId) {
        groupRepository.removeGroupAdmin(groupId,userId);
    }

    @Override
    public void removeGroup(Long groupId) {
        groupRepository.removeAllGroupUsers(groupId);
        groupRepository.removeAllGroupAdmins(groupId);
        groupRepository.removeAllGroupTeachers(groupId);
        groupRepository.removeGroup(groupId);
    }

    @Override
    public List<Long> getGroupUsers(Long groupId) {
       return groupRepository.getGroupUsers(groupId);
    }

    @Override
    public List<Long> getGroupTeachers(Long groupId) {
        return groupRepository.getGroupTeachers(groupId);
    }

    @Override
    public List<Long> getUserGroupsId(Long userId) {
        return groupRepository.getUserGroupsId(userId);
    }

}
