package com.id.kusumakazu.repository;

import com.id.kusumakazu.domain.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the GroupChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupChatRepository extends MongoRepository<GroupChat, String> {}
