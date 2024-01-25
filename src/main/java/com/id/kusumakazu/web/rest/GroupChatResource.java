package com.id.kusumakazu.web.rest;

import com.id.kusumakazu.repository.GroupChatRepository;
import com.id.kusumakazu.service.GroupChatService;
import com.id.kusumakazu.service.dto.GroupChatDTO;
import com.id.kusumakazu.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.id.kusumakazu.domain.GroupChat}.
 */
@RestController
@RequestMapping("/api")
public class GroupChatResource {

    private final Logger log = LoggerFactory.getLogger(GroupChatResource.class);

    private static final String ENTITY_NAME = "microchatsvcGroupChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupChatService groupChatService;

    private final GroupChatRepository groupChatRepository;

    public GroupChatResource(GroupChatService groupChatService, GroupChatRepository groupChatRepository) {
        this.groupChatService = groupChatService;
        this.groupChatRepository = groupChatRepository;
    }

    /**
     * {@code POST  /group-chats} : Create a new groupChat.
     *
     * @param groupChatDTO the groupChatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupChatDTO, or with status {@code 400 (Bad Request)} if the groupChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/group-chats")
    public ResponseEntity<GroupChatDTO> createGroupChat(@Valid @RequestBody GroupChatDTO groupChatDTO) throws URISyntaxException {
        log.debug("REST request to save GroupChat : {}", groupChatDTO);
        if (groupChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new groupChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroupChatDTO result = groupChatService.save(groupChatDTO);
        return ResponseEntity
            .created(new URI("/api/group-chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /group-chats/:id} : Updates an existing groupChat.
     *
     * @param id the id of the groupChatDTO to save.
     * @param groupChatDTO the groupChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/group-chats/{id}")
    public ResponseEntity<GroupChatDTO> updateGroupChat(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody GroupChatDTO groupChatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GroupChat : {}, {}", id, groupChatDTO);
        if (groupChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GroupChatDTO result = groupChatService.update(groupChatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupChatDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /group-chats/:id} : Partial updates given fields of an existing groupChat, field will ignore if it is null
     *
     * @param id the id of the groupChatDTO to save.
     * @param groupChatDTO the groupChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupChatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/group-chats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GroupChatDTO> partialUpdateGroupChat(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody GroupChatDTO groupChatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GroupChat partially : {}, {}", id, groupChatDTO);
        if (groupChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GroupChatDTO> result = groupChatService.partialUpdate(groupChatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupChatDTO.getId())
        );
    }

    /**
     * {@code GET  /group-chats} : get all the groupChats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupChats in body.
     */
    @GetMapping("/group-chats")
    public List<GroupChatDTO> getAllGroupChats() {
        log.debug("REST request to get all GroupChats");
        return groupChatService.findAll();
    }

    /**
     * {@code GET  /group-chats/:id} : get the "id" groupChat.
     *
     * @param id the id of the groupChatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupChatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/group-chats/{id}")
    public ResponseEntity<GroupChatDTO> getGroupChat(@PathVariable String id) {
        log.debug("REST request to get GroupChat : {}", id);
        Optional<GroupChatDTO> groupChatDTO = groupChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupChatDTO);
    }

    /**
     * {@code DELETE  /group-chats/:id} : delete the "id" groupChat.
     *
     * @param id the id of the groupChatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/group-chats/{id}")
    public ResponseEntity<Void> deleteGroupChat(@PathVariable String id) {
        log.debug("REST request to delete GroupChat : {}", id);
        groupChatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
