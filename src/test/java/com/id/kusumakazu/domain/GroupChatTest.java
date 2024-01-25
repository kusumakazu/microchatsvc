package com.id.kusumakazu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.id.kusumakazu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupChat.class);
        GroupChat groupChat1 = new GroupChat();
        groupChat1.setId("id1");
        GroupChat groupChat2 = new GroupChat();
        groupChat2.setId(groupChat1.getId());
        assertThat(groupChat1).isEqualTo(groupChat2);
        groupChat2.setId("id2");
        assertThat(groupChat1).isNotEqualTo(groupChat2);
        groupChat1.setId(null);
        assertThat(groupChat1).isNotEqualTo(groupChat2);
    }
}
