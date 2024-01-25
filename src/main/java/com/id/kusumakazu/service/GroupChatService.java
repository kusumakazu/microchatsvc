package com.id.kusumakazu.service;

import com.id.kusumakazu.service.dto.GroupChatDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.id.kusumakazu.domain.GroupChat}.
 */
public interface GroupChatService {
    /**
     * Save a groupChat.
     *
     * @param groupChatDTO the entity to save.
     * @return the persisted entity.
     */
    GroupChatDTO save(GroupChatDTO groupChatDTO);

    /**
     * Updates a groupChat.
     *
     * @param groupChatDTO the entity to update.
     * @return the persisted entity.
     */
    GroupChatDTO update(GroupChatDTO groupChatDTO);

    /**
     * Partially updates a groupChat.
     *
     * @param groupChatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GroupChatDTO> partialUpdate(GroupChatDTO groupChatDTO);

    /**
     * Get all the groupChats.
     *
     * @return the list of entities.
     */
    List<GroupChatDTO> findAll();

    /**
     * Get the "id" groupChat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GroupChatDTO> findOne(String id);

    /**
     * Delete the "id" groupChat.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
