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

    Optional<Group> findById(Long groupId);

    @Transactional
    @Modifying
    @Query(value = "SELECT user_id FROM groups_users WHERE group_id=(:groupId)", nativeQuery = true)
    List<Long> getGroupUsers(@Param("groupId") Long groupId);

    @Transactional
    @Modifying
    @Query(value = "SELECT group_id FROM groups_users WHERE user_id=(:userId)", nativeQuery = true)
    List<Long> getUserGroupsId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "SELECT user_id FROM groups_users WHERE group_id=(:groupId)", nativeQuery = true)
    List<Long> getTeacherGroupsId(@Param("groupId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "SELECT user_id FROM groups_teachers WHERE group_id=(:groupId)", nativeQuery = true)
    List<Long> getGroupTeachers(@Param("groupId") Long groupId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO groups_users(group_id,user_id) VALUES(:groupId, :userId)", nativeQuery = true)
    void addGroupUser(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO groups_teachers(group_id,user_id) VALUES(:groupId, :userId)", nativeQuery = true)
    void addGroupTeacher(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO groups_admins(group_id,user_id) VALUES(:groupId, :userId)", nativeQuery = true)
    void addGroupAdmin(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_users WHERE group_id=:groupId AND user_id=:userId", nativeQuery = true)
    void removeGroupUser(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_teachers WHERE group_id=:groupId AND user_id=:userId", nativeQuery = true)
    void removeGroupTeacher(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_admins WHERE group_id=:groupId AND user_id=:userId", nativeQuery = true)
    void removeGroupAdmin(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_users WHERE group_id=:groupId", nativeQuery = true)
    void removeAllGroupUsers(@Param("groupId") Long groupId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_admins WHERE group_id=:groupId", nativeQuery = true)
    void removeAllGroupAdmins(@Param("groupId") Long groupId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM groups_teachers WHERE group_id=:groupId", nativeQuery = true)
    void removeAllGroupTeachers(@Param("groupId") Long groupId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM learning_groups WHERE id=:groupId \n", nativeQuery = true)
    void removeGroup(@Param("groupId") Long groupId);

}