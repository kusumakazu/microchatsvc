package com.id.kusumakazu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAccountMapperTest {

    private UserAccountMapper userAccountMapper;

    @BeforeEach
    public void setUp() {
        userAccountMapper = new UserAccountMapperImpl();
    }
}
