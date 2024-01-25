package com.id.kusumakazu.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Message.
 */
@Document(collection = "message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("content")
    private String content;

    @NotNull
    @Field("timestamp")
    private Instant timestamp;

    @Field("sender_id")
    private Long senderId;

    @Field("recipient_id")
    private Long recipientId;

    @Field("is_sent")
    private Boolean isSent;

    @Field("is_read")
    private Boolean isRead;

    @DBRef
    @Field("sender")
    private UserAccount sender;

    @DBRef
    @Field("recipient")
    private UserAccount recipient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Message id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Message content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Message timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public Message senderId(Long senderId) {
        this.setSenderId(senderId);
        return this;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return this.recipientId;
    }

    public Message recipientId(Long recipientId) {
        this.setRecipientId(recipientId);
        return this;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsSent() {
        return this.isSent;
    }

    public Message isSent(Boolean isSent) {
        this.setIsSent(isSent);
        return this;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public Message isRead(Boolean isRead) {
        this.setIsRead(isRead);
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public UserAccount getSender() {
        return this.sender;
    }

    public void setSender(UserAccount userAccount) {
        this.sender = userAccount;
    }

    public Message sender(UserAccount userAccount) {
        this.setSender(userAccount);
        return this;
    }

    public UserAccount getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserAccount userAccount) {
        this.recipient = userAccount;
    }

    public Message recipient(UserAccount userAccount) {
        this.setRecipient(userAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", senderId=" + getSenderId() +
            ", recipientId=" + getRecipientId() +
            ", isSent='" + getIsSent() + "'" +
            ", isRead='" + getIsRead() + "'" +
            "}";
    }
}
