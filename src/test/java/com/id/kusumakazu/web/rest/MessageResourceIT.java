package com.id.kusumakazu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.id.kusumakazu.IntegrationTest;
import com.id.kusumakazu.domain.Message;
import com.id.kusumakazu.repository.MessageRepository;
import com.id.kusumakazu.service.dto.MessageDTO;
import com.id.kusumakazu.service.mapper.MessageMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SENDER_ID = 1L;
    private static final Long UPDATED_SENDER_ID = 2L;

    private static final Long DEFAULT_RECIPIENT_ID = 1L;
    private static final Long UPDATED_RECIPIENT_ID = 2L;

    private static final Boolean DEFAULT_IS_SENT = false;
    private static final Boolean UPDATED_IS_SENT = true;

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MockMvc restMessageMockMvc;

    private Message message;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity() {
        Message message = new Message()
            .content(DEFAULT_CONTENT)
            .timestamp(DEFAULT_TIMESTAMP)
            .senderId(DEFAULT_SENDER_ID)
            .recipientId(DEFAULT_RECIPIENT_ID)
            .isSent(DEFAULT_IS_SENT)
            .isRead(DEFAULT_IS_READ);
        return message;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity() {
        Message message = new Message()
            .content(UPDATED_CONTENT)
            .timestamp(UPDATED_TIMESTAMP)
            .senderId(UPDATED_SENDER_ID)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSent(UPDATED_IS_SENT)
            .isRead(UPDATED_IS_READ);
        return message;
    }

    @BeforeEach
    public void initTest() {
        messageRepository.deleteAll();
        message = createEntity();
    }

    @Test
    void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();
        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMessage.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testMessage.getSenderId()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testMessage.getRecipientId()).isEqualTo(DEFAULT_RECIPIENT_ID);
        assertThat(testMessage.getIsSent()).isEqualTo(DEFAULT_IS_SENT);
        assertThat(testMessage.getIsRead()).isEqualTo(DEFAULT_IS_READ);
    }

    @Test
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId("existing_id");
        MessageDTO messageDTO = messageMapper.toDto(message);

        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setContent(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setTimestamp(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        // Get all the messageList
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].recipientId").value(hasItem(DEFAULT_RECIPIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].isSent").value(hasItem(DEFAULT_IS_SENT.booleanValue())))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())));
    }

    @Test
    void getMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        // Get the message
        restMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.senderId").value(DEFAULT_SENDER_ID.intValue()))
            .andExpect(jsonPath("$.recipientId").value(DEFAULT_RECIPIENT_ID.intValue()))
            .andExpect(jsonPath("$.isSent").value(DEFAULT_IS_SENT.booleanValue()))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.booleanValue()));
    }

    @Test
    void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        updatedMessage
            .content(UPDATED_CONTENT)
            .timestamp(UPDATED_TIMESTAMP)
            .senderId(UPDATED_SENDER_ID)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSent(UPDATED_IS_SENT)
            .isRead(UPDATED_IS_READ);
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMessage.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testMessage.getSenderId()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testMessage.getRecipientId()).isEqualTo(UPDATED_RECIPIENT_ID);
        assertThat(testMessage.getIsSent()).isEqualTo(UPDATED_IS_SENT);
        assertThat(testMessage.getIsRead()).isEqualTo(UPDATED_IS_READ);
    }

    @Test
    void putNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage.isSent(UPDATED_IS_SENT).isRead(UPDATED_IS_READ);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMessage.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testMessage.getSenderId()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testMessage.getRecipientId()).isEqualTo(DEFAULT_RECIPIENT_ID);
        assertThat(testMessage.getIsSent()).isEqualTo(UPDATED_IS_SENT);
        assertThat(testMessage.getIsRead()).isEqualTo(UPDATED_IS_READ);
    }

    @Test
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage
            .content(UPDATED_CONTENT)
            .timestamp(UPDATED_TIMESTAMP)
            .senderId(UPDATED_SENDER_ID)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSent(UPDATED_IS_SENT)
            .isRead(UPDATED_IS_READ);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMessage.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testMessage.getSenderId()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testMessage.getRecipientId()).isEqualTo(UPDATED_RECIPIENT_ID);
        assertThat(testMessage.getIsSent()).isEqualTo(UPDATED_IS_SENT);
        assertThat(testMessage.getIsRead()).isEqualTo(UPDATED_IS_READ);
    }

    @Test
    void patchNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(messageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, message.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
