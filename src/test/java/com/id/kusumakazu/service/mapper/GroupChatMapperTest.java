package com.id.kusumakazu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupChatMapperTest {

    private GroupChatMapper groupChatMapper;

    @BeforeEach
    public void setUp() {
        groupChatMapper = new GroupChatMapperImpl();
    }
}
