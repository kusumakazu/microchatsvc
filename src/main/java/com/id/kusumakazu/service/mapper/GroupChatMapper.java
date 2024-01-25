package com.id.kusumakazu.service.mapper;

import com.id.kusumakazu.domain.GroupChat;
import com.id.kusumakazu.domain.UserAccount;
import com.id.kusumakazu.service.dto.GroupChatDTO;
import com.id.kusumakazu.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GroupChat} and its DTO {@link GroupChatDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupChatMapper extends EntityMapper<GroupChatDTO, GroupChat> {
    @Mapping(target = "members", source = "members", qualifiedByName = "userAccountId")
    GroupChatDTO toDto(GroupChat s);

    @Named("userAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAccountDTO toDtoUserAccountId(UserAccount userAccount);
}
