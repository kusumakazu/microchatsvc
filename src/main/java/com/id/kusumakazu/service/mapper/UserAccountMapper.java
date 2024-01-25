package com.id.kusumakazu.service.mapper;

import com.id.kusumakazu.domain.UserAccount;
import com.id.kusumakazu.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {}
