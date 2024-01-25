package com.id.kusumakazu.service;

import com.id.kusumakazu.service.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.id.kusumakazu.domain.UserAccount}.
 */
public interface UserAccountService {
    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    UserAccountDTO save(UserAccountDTO userAccountDTO);

    /**
     * Updates a userAccount.
     *
     * @param userAccountDTO the entity to update.
     * @return the persisted entity.
     */
    UserAccountDTO update(UserAccountDTO userAccountDTO);

    /**
     * Partially updates a userAccount.
     *
     * @param userAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO);

    /**
     * Get all the userAccounts.
     *
     * @return the list of entities.
     */
    List<UserAccountDTO> findAll();

    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccountDTO> findOne(String id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
