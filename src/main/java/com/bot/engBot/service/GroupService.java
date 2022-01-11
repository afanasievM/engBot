package com.bot.engBot.service;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Group;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface GroupService {
    void save(Group group);
    List<Group> findAllByOwnerId(Long ownerId);
    Optional<Group> findByGroupNameAndOwnerId(String groupName, Long ownerId);
    Optional<Group> findByGroupName(String groupName);
    Optional<Group> findById(Long groupId);
    void addGroupUser (Long groupId, Long userId);
    void addGroupTeacher (@Param("groupId") Long groupId, @Param("userId") Long userId);
    void addGroupAdmin (@Param("groupId") Long groupId, @Param("userId") Long userId);

    void removeGroupUser (@Param("groupId") Long groupId, @Param("userId") Long userId);
    void removeGroupTeacher (@Param("groupId") Long groupId, @Param("userId") Long userId);
    void removeGroupAdmin (@Param("groupId") Long groupId, @Param("userId") Long userId);
    void removeGroup (@Param("groupId") Long groupId);
    List<Long> getGroupUsers (@Param("groupId") Long groupId);
    List<Long> getGroupTeachers(@Param("groupId") Long groupId);
    List<Long> getUserGroupsId (@Param("userId") Long userId);
}
