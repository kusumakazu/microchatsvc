package com.id.kusumakazu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.id.kusumakazu.domain.GroupChat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupChatDTO implements Serializable {

    private String id;

    @NotNull
    private String groupName;

    private String groupUserAccountId;

    private String groupLogoId;

    private UserAccountDTO members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUserAccountId() {
        return groupUserAccountId;
    }

    public void setGroupUserAccountId(String groupUserAccountId) {
        this.groupUserAccountId = groupUserAccountId;
    }

    public String getGroupLogoId() {
        return groupLogoId;
    }

    public void setGroupLogoId(String groupLogoId) {
        this.groupLogoId = groupLogoId;
    }

    public UserAccountDTO getMembers() {
        return members;
    }

    public void setMembers(UserAccountDTO members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupChatDTO)) {
            return false;
        }

        GroupChatDTO groupChatDTO = (GroupChatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupChatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupChatDTO{" +
            "id='" + getId() + "'" +
            ", groupName='" + getGroupName() + "'" +
            ", groupUserAccountId='" + getGroupUserAccountId() + "'" +
            ", groupLogoId='" + getGroupLogoId() + "'" +
            ", members=" + getMembers() +
            "}";
    }
}
