package com.bot.engBot.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByOwnerId(Long ownerId);
    Optional<Group> findByGroupNameAndOwnerId(String groupName, Long ownerId);
    Optional<Group> findByGroupName(String groupName);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO groups_users(group_id,user_id) VALUES(:groupId, :userId)", nativeQuery = true)
    void addGroupUser (@Param("groupId") Long groupId, @Param("userId") Long userId);
}