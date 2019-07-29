package com.rez.melee.slippi.service.impl;

import com.rez.melee.slippi.service.SlippiFileService;
import com.rez.melee.slippi.domain.SlippiFile;
import com.rez.melee.slippi.repository.SlippiFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SlippiFile}.
 */
@Service
public class SlippiFileServiceImpl implements SlippiFileService {

    private final Logger log = LoggerFactory.getLogger(SlippiFileServiceImpl.class);

    private final SlippiFileRepository slippiFileRepository;

    public SlippiFileServiceImpl(SlippiFileRepository slippiFileRepository) {
        this.slippiFileRepository = slippiFileRepository;
    }

    /**
     * Save a slippiFile.
     *
     * @param slippiFile the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SlippiFile save(SlippiFile slippiFile) {
        log.debug("Request to save SlippiFile : {}", slippiFile);
        return slippiFileRepository.save(slippiFile);
    }

    /**
     * Get all the slippiFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<SlippiFile> findAll(Pageable pageable) {
        log.debug("Request to get all SlippiFiles");
        return slippiFileRepository.findAll(pageable);
    }


    /**
     * Get one slippiFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<SlippiFile> findOne(String id) {
        log.debug("Request to get SlippiFile : {}", id);
        return slippiFileRepository.findById(id);
    }

    /**
     * Delete the slippiFile by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SlippiFile : {}", id);
        slippiFileRepository.deleteById(id);
    }
}
