package com.id.kusumakazu.service.impl;

import com.id.kusumakazu.domain.GroupChat;
import com.id.kusumakazu.repository.GroupChatRepository;
import com.id.kusumakazu.service.GroupChatService;
import com.id.kusumakazu.service.dto.GroupChatDTO;
import com.id.kusumakazu.service.mapper.GroupChatMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link GroupChat}.
 */
@Service
public class GroupChatServiceImpl implements GroupChatService {

    private final Logger log = LoggerFactory.getLogger(GroupChatServiceImpl.class);

    private final GroupChatRepository groupChatRepository;

    private final GroupChatMapper groupChatMapper;

    public GroupChatServiceImpl(GroupChatRepository groupChatRepository, GroupChatMapper groupChatMapper) {
        this.groupChatRepository = groupChatRepository;
        this.groupChatMapper = groupChatMapper;
    }

    @Override
    public GroupChatDTO save(GroupChatDTO groupChatDTO) {
        log.debug("Request to save GroupChat : {}", groupChatDTO);
        GroupChat groupChat = groupChatMapper.toEntity(groupChatDTO);
        groupChat = groupChatRepository.save(groupChat);
        return groupChatMapper.toDto(groupChat);
    }

    @Override
    public GroupChatDTO update(GroupChatDTO groupChatDTO) {
        log.debug("Request to update GroupChat : {}", groupChatDTO);
        GroupChat groupChat = groupChatMapper.toEntity(groupChatDTO);
        groupChat = groupChatRepository.save(groupChat);
        return groupChatMapper.toDto(groupChat);
    }

    @Override
    public Optional<GroupChatDTO> partialUpdate(GroupChatDTO groupChatDTO) {
        log.debug("Request to partially update GroupChat : {}", groupChatDTO);

        return groupChatRepository
            .findById(groupChatDTO.getId())
            .map(existingGroupChat -> {
                groupChatMapper.partialUpdate(existingGroupChat, groupChatDTO);

                return existingGroupChat;
            })
            .map(groupChatRepository::save)
            .map(groupChatMapper::toDto);
    }

    @Override
    public List<GroupChatDTO> findAll() {
        log.debug("Request to get all GroupChats");
        return groupChatRepository.findAll().stream().map(groupChatMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<GroupChatDTO> findOne(String id) {
        log.debug("Request to get GroupChat : {}", id);
        return groupChatRepository.findById(id).map(groupChatMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete GroupChat : {}", id);
        groupChatRepository.deleteById(id);
    }
}
