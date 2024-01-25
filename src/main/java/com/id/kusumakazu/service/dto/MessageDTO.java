package com.id.kusumakazu.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.id.kusumakazu.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageDTO implements Serializable {

    private String id;

    @NotNull
    private String content;

    @NotNull
    private Instant timestamp;

    private Long senderId;

    private Long recipientId;

    private Boolean isSent;

    private Boolean isRead;

    private UserAccountDTO sender;

    private UserAccountDTO recipient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public UserAccountDTO getSender() {
        return sender;
    }

    public void setSender(UserAccountDTO sender) {
        this.sender = sender;
    }

    public UserAccountDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserAccountDTO recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id='" + getId() + "'" +
            ", content='" + getContent() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", senderId=" + getSenderId() +
            ", recipientId=" + getRecipientId() +
            ", isSent='" + getIsSent() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", sender=" + getSender() +
            ", recipient=" + getRecipient() +
            "}";
    }
}
