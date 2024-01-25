package com.id.kusumakazu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.id.kusumakazu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupChatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupChatDTO.class);
        GroupChatDTO groupChatDTO1 = new GroupChatDTO();
        groupChatDTO1.setId("id1");
        GroupChatDTO groupChatDTO2 = new GroupChatDTO();
        assertThat(groupChatDTO1).isNotEqualTo(groupChatDTO2);
        groupChatDTO2.setId(groupChatDTO1.getId());
        assertThat(groupChatDTO1).isEqualTo(groupChatDTO2);
        groupChatDTO2.setId("id2");
        assertThat(groupChatDTO1).isNotEqualTo(groupChatDTO2);
        groupChatDTO1.setId(null);
        assertThat(groupChatDTO1).isNotEqualTo(groupChatDTO2);
    }
}
