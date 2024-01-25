package com.id.kusumakazu.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A GroupChat.
 */
@Document(collection = "group_chat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("group_name")
    private String groupName;

    @Field("group_user_account_id")
    private String groupUserAccountId;

    @Field("group_logo_id")
    private String groupLogoId;

    @DBRef
    @Field("members")
    private UserAccount members;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public GroupChat id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public GroupChat groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUserAccountId() {
        return this.groupUserAccountId;
    }

    public GroupChat groupUserAccountId(String groupUserAccountId) {
        this.setGroupUserAccountId(groupUserAccountId);
        return this;
    }

    public void setGroupUserAccountId(String groupUserAccountId) {
        this.groupUserAccountId = groupUserAccountId;
    }

    public String getGroupLogoId() {
        return this.groupLogoId;
    }

    public GroupChat groupLogoId(String groupLogoId) {
        this.setGroupLogoId(groupLogoId);
        return this;
    }

    public void setGroupLogoId(String groupLogoId) {
        this.groupLogoId = groupLogoId;
    }

    public UserAccount getMembers() {
        return this.members;
    }

    public void setMembers(UserAccount userAccount) {
        this.members = userAccount;
    }

    public GroupChat members(UserAccount userAccount) {
        this.setMembers(userAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupChat)) {
            return false;
        }
        return id != null && id.equals(((GroupChat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupChat{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", groupUserAccountId='" + getGroupUserAccountId() + "'" +
            ", groupLogoId='" + getGroupLogoId() + "'" +
            "}";
    }
}
