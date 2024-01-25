package com.id.kusumakazu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.id.kusumakazu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccount.class);
        UserAccount userAccount1 = new UserAccount();
        userAccount1.setId("id1");
        UserAccount userAccount2 = new UserAccount();
        userAccount2.setId(userAccount1.getId());
        assertThat(userAccount1).isEqualTo(userAccount2);
        userAccount2.setId("id2");
        assertThat(userAccount1).isNotEqualTo(userAccount2);
        userAccount1.setId(null);
        assertThat(userAccount1).isNotEqualTo(userAccount2);
    }
}
