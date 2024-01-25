package com.id.kusumakazu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.id.kusumakazu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageDTO.class);
        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setId("id1");
        MessageDTO messageDTO2 = new MessageDTO();
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
        messageDTO2.setId(messageDTO1.getId());
        assertThat(messageDTO1).isEqualTo(messageDTO2);
        messageDTO2.setId("id2");
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
        messageDTO1.setId(null);
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
    }
}
