package com.rez.melee.slippi.web.rest;

import com.rez.melee.slippi.domain.SlippiFile;
import com.rez.melee.slippi.service.SlippiFileService;
import com.rez.melee.slippi.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.rez.melee.slippi.domain.SlippiFile}.
 */
@RestController
@RequestMapping("/api")
public class SlippiFileResource {

    private final Logger log = LoggerFactory.getLogger(SlippiFileResource.class);

    private static final String ENTITY_NAME = "uploadserviceSlippiFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlippiFileService slippiFileService;

    public SlippiFileResource(SlippiFileService slippiFileService) {
        this.slippiFileService = slippiFileService;
    }

    /**
     * {@code POST  /slippi-files} : Create a new slippiFile.
     *
     * @param slippiFile the slippiFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slippiFile, or with status {@code 400 (Bad Request)} if the slippiFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/slippi-files")
    public ResponseEntity<SlippiFile> createSlippiFile(@Valid @RequestBody SlippiFile slippiFile) throws URISyntaxException {
        log.debug("REST request to save SlippiFile : {}", slippiFile);
        if (slippiFile.getId() != null) {
            throw new BadRequestAlertException("A new slippiFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlippiFile result = slippiFileService.save(slippiFile);
        return ResponseEntity.created(new URI("/api/slippi-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /slippi-files} : Updates an existing slippiFile.
     *
     * @param slippiFile the slippiFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slippiFile,
     * or with status {@code 400 (Bad Request)} if the slippiFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slippiFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/slippi-files")
    public ResponseEntity<SlippiFile> updateSlippiFile(@Valid @RequestBody SlippiFile slippiFile) throws URISyntaxException {
        log.debug("REST request to update SlippiFile : {}", slippiFile);
        if (slippiFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SlippiFile result = slippiFileService.save(slippiFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slippiFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /slippi-files} : get all the slippiFiles.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slippiFiles in body.
     */
    @GetMapping("/slippi-files")
    public ResponseEntity<List<SlippiFile>> getAllSlippiFiles(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of SlippiFiles");
        Page<SlippiFile> page = slippiFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /slippi-files/:id} : get the "id" slippiFile.
     *
     * @param id the id of the slippiFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slippiFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/slippi-files/{id}")
    public ResponseEntity<SlippiFile> getSlippiFile(@PathVariable String id) {
        log.debug("REST request to get SlippiFile : {}", id);
        Optional<SlippiFile> slippiFile = slippiFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slippiFile);
    }

    /**
     * {@code DELETE  /slippi-files/:id} : delete the "id" slippiFile.
     *
     * @param id the id of the slippiFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/slippi-files/{id}")
    public ResponseEntity<Void> deleteSlippiFile(@PathVariable String id) {
        log.debug("REST request to delete SlippiFile : {}", id);
        slippiFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
