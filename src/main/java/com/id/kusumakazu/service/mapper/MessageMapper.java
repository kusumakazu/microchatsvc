package com.id.kusumakazu.service.mapper;

import com.id.kusumakazu.domain.Message;
import com.id.kusumakazu.domain.UserAccount;
import com.id.kusumakazu.service.dto.MessageDTO;
import com.id.kusumakazu.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "sender", source = "sender", qualifiedByName = "userAccountId")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "userAccountId")
    MessageDTO toDto(Message s);

    @Named("userAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAccountDTO toDtoUserAccountId(UserAccount userAccount);
}
