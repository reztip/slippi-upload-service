package com.rez.melee.slippi.service;

import com.rez.melee.slippi.domain.SlippiFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

/**
 * Service Interface for managing {@link SlippiFile}.
 */
public interface SlippiFileService {

    /**
     * Save a slippiFile.
     *
     * @param slippiFile the entity to save.
     * @return the persisted entity.
     */
    SlippiFile save(SlippiFile slippiFile) throws IOException;

    /**
     * Get all the slippiFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SlippiFile> findAll(Pageable pageable);


    /**
     * Get the "id" slippiFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SlippiFile> findOne(String id);

    /**
     * Delete the "id" slippiFile.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Populates the hashValue of the slippiFile, if not set.
     * @param slippiFile
     */
    void populateHashValue(SlippiFile slippiFile) throws IOException;

    /**
     * Throws an exception if the id / hash is not unique.
     */
    void validateUniqueHash(SlippiFile slippiFile);
}
