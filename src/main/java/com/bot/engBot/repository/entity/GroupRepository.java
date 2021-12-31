package com.bot.engBot.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByOwnerId(Long ownerId);
    Optional<Group> findByGroupNameAndOwnerId(String groupName, Long ownerId);
    Optional<Group> findByGroupName(String groupName);
}