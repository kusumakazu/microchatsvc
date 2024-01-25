package com.id.kusumakazu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.id.kusumakazu.IntegrationTest;
import com.id.kusumakazu.domain.GroupChat;
import com.id.kusumakazu.repository.GroupChatRepository;
import com.id.kusumakazu.service.dto.GroupChatDTO;
import com.id.kusumakazu.service.mapper.GroupChatMapper;
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
 * Integration tests for the {@link GroupChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupChatResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_USER_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_USER_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_LOGO_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_LOGO_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/group-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatMapper groupChatMapper;

    @Autowired
    private MockMvc restGroupChatMockMvc;

    private GroupChat groupChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChat createEntity() {
        GroupChat groupChat = new GroupChat()
            .groupName(DEFAULT_GROUP_NAME)
            .groupUserAccountId(DEFAULT_GROUP_USER_ACCOUNT_ID)
            .groupLogoId(DEFAULT_GROUP_LOGO_ID);
        return groupChat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChat createUpdatedEntity() {
        GroupChat groupChat = new GroupChat()
            .groupName(UPDATED_GROUP_NAME)
            .groupUserAccountId(UPDATED_GROUP_USER_ACCOUNT_ID)
            .groupLogoId(UPDATED_GROUP_LOGO_ID);
        return groupChat;
    }

    @BeforeEach
    public void initTest() {
        groupChatRepository.deleteAll();
        groupChat = createEntity();
    }

    @Test
    void createGroupChat() throws Exception {
        int databaseSizeBeforeCreate = groupChatRepository.findAll().size();
        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);
        restGroupChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupChatDTO)))
            .andExpect(status().isCreated());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeCreate + 1);
        GroupChat testGroupChat = groupChatList.get(groupChatList.size() - 1);
        assertThat(testGroupChat.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroupChat.getGroupUserAccountId()).isEqualTo(DEFAULT_GROUP_USER_ACCOUNT_ID);
        assertThat(testGroupChat.getGroupLogoId()).isEqualTo(DEFAULT_GROUP_LOGO_ID);
    }

    @Test
    void createGroupChatWithExistingId() throws Exception {
        // Create the GroupChat with an existing ID
        groupChat.setId("existing_id");
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        int databaseSizeBeforeCreate = groupChatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupChatRepository.findAll().size();
        // set the field null
        groupChat.setGroupName(null);

        // Create the GroupChat, which fails.
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        restGroupChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupChatDTO)))
            .andExpect(status().isBadRequest());

        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGroupChats() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        // Get all the groupChatList
        restGroupChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupChat.getId())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].groupUserAccountId").value(hasItem(DEFAULT_GROUP_USER_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].groupLogoId").value(hasItem(DEFAULT_GROUP_LOGO_ID)));
    }

    @Test
    void getGroupChat() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        // Get the groupChat
        restGroupChatMockMvc
            .perform(get(ENTITY_API_URL_ID, groupChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupChat.getId()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.groupUserAccountId").value(DEFAULT_GROUP_USER_ACCOUNT_ID))
            .andExpect(jsonPath("$.groupLogoId").value(DEFAULT_GROUP_LOGO_ID));
    }

    @Test
    void getNonExistingGroupChat() throws Exception {
        // Get the groupChat
        restGroupChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingGroupChat() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();

        // Update the groupChat
        GroupChat updatedGroupChat = groupChatRepository.findById(groupChat.getId()).get();
        updatedGroupChat.groupName(UPDATED_GROUP_NAME).groupUserAccountId(UPDATED_GROUP_USER_ACCOUNT_ID).groupLogoId(UPDATED_GROUP_LOGO_ID);
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(updatedGroupChat);

        restGroupChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupChatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isOk());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
        GroupChat testGroupChat = groupChatList.get(groupChatList.size() - 1);
        assertThat(testGroupChat.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroupChat.getGroupUserAccountId()).isEqualTo(UPDATED_GROUP_USER_ACCOUNT_ID);
        assertThat(testGroupChat.getGroupLogoId()).isEqualTo(UPDATED_GROUP_LOGO_ID);
    }

    @Test
    void putNonExistingGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupChatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupChatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGroupChatWithPatch() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();

        // Update the groupChat using partial update
        GroupChat partialUpdatedGroupChat = new GroupChat();
        partialUpdatedGroupChat.setId(groupChat.getId());

        restGroupChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupChat))
            )
            .andExpect(status().isOk());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
        GroupChat testGroupChat = groupChatList.get(groupChatList.size() - 1);
        assertThat(testGroupChat.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroupChat.getGroupUserAccountId()).isEqualTo(DEFAULT_GROUP_USER_ACCOUNT_ID);
        assertThat(testGroupChat.getGroupLogoId()).isEqualTo(DEFAULT_GROUP_LOGO_ID);
    }

    @Test
    void fullUpdateGroupChatWithPatch() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();

        // Update the groupChat using partial update
        GroupChat partialUpdatedGroupChat = new GroupChat();
        partialUpdatedGroupChat.setId(groupChat.getId());

        partialUpdatedGroupChat
            .groupName(UPDATED_GROUP_NAME)
            .groupUserAccountId(UPDATED_GROUP_USER_ACCOUNT_ID)
            .groupLogoId(UPDATED_GROUP_LOGO_ID);

        restGroupChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupChat))
            )
            .andExpect(status().isOk());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
        GroupChat testGroupChat = groupChatList.get(groupChatList.size() - 1);
        assertThat(testGroupChat.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroupChat.getGroupUserAccountId()).isEqualTo(UPDATED_GROUP_USER_ACCOUNT_ID);
        assertThat(testGroupChat.getGroupLogoId()).isEqualTo(UPDATED_GROUP_LOGO_ID);
    }

    @Test
    void patchNonExistingGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, groupChatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGroupChat() throws Exception {
        int databaseSizeBeforeUpdate = groupChatRepository.findAll().size();
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupChatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(groupChatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GroupChat in the database
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGroupChat() throws Exception {
        // Initialize the database
        groupChatRepository.save(groupChat);

        int databaseSizeBeforeDelete = groupChatRepository.findAll().size();

        // Delete the groupChat
        restGroupChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, groupChat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GroupChat> groupChatList = groupChatRepository.findAll();
        assertThat(groupChatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
