package com.id.kusumakazu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.id.kusumakazu.IntegrationTest;
import com.id.kusumakazu.domain.UserAccount;
import com.id.kusumakazu.repository.UserAccountRepository;
import com.id.kusumakazu.service.dto.UserAccountDTO;
import com.id.kusumakazu.service.mapper.UserAccountMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccountResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private MockMvc restUserAccountMockMvc;

    private UserAccount userAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity() {
        UserAccount userAccount = new UserAccount().username(DEFAULT_USERNAME).displayName(DEFAULT_DISPLAY_NAME).password(DEFAULT_PASSWORD);
        return userAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity() {
        UserAccount userAccount = new UserAccount().username(UPDATED_USERNAME).displayName(UPDATED_DISPLAY_NAME).password(UPDATED_PASSWORD);
        return userAccount;
    }

    @BeforeEach
    public void initTest() {
        userAccountRepository.deleteAll();
        userAccount = createEntity();
    }

    @Test
    void createUserAccount() throws Exception {
        int databaseSizeBeforeCreate = userAccountRepository.findAll().size();
        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        restUserAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate + 1);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAccount.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testUserAccount.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    void createUserAccountWithExistingId() throws Exception {
        // Create the UserAccount with an existing ID
        userAccount.setId("existing_id");
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        int databaseSizeBeforeCreate = userAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().size();
        // set the field null
        userAccount.setUsername(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().size();
        // set the field null
        userAccount.setDisplayName(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAccountRepository.findAll().size();
        // set the field null
        userAccount.setPassword(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserAccounts() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        // Get all the userAccountList
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccount.getId())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    void getUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        // Get the userAccount
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccount.getId()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    void getNonExistingUserAccount() throws Exception {
        // Get the userAccount
        restUserAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).get();
        updatedUserAccount.username(UPDATED_USERNAME).displayName(UPDATED_DISPLAY_NAME).password(UPDATED_PASSWORD);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(updatedUserAccount);

        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAccount.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUserAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void putNonExistingUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount.username(UPDATED_USERNAME).displayName(UPDATED_DISPLAY_NAME).password(UPDATED_PASSWORD);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAccount.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUserAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void fullUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount.username(UPDATED_USERNAME).displayName(UPDATED_DISPLAY_NAME).password(UPDATED_PASSWORD);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
        UserAccount testUserAccount = userAccountList.get(userAccountList.size() - 1);
        assertThat(testUserAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAccount.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUserAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void patchNonExistingUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserAccount() throws Exception {
        int databaseSizeBeforeUpdate = userAccountRepository.findAll().size();
        userAccount.setId(UUID.randomUUID().toString());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserAccount() throws Exception {
        // Initialize the database
        userAccountRepository.save(userAccount);

        int databaseSizeBeforeDelete = userAccountRepository.findAll().size();

        // Delete the userAccount
        restUserAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        assertThat(userAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
