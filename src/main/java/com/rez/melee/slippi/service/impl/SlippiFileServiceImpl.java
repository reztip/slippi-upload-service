package com.rez.melee.slippi.service.impl;

import com.rez.melee.slippi.domain.SlippiFile;
import com.rez.melee.slippi.repository.SlippiFileRepository;
import com.rez.melee.slippi.service.S3UploadService;
import com.rez.melee.slippi.service.SlippiFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SlippiFile}.
 */
@Service
public class SlippiFileServiceImpl implements SlippiFileService {

    private final Logger log = LoggerFactory.getLogger(SlippiFileServiceImpl.class);

    private final SlippiFileRepository slippiFileRepository;

    @Autowired
    private S3UploadService s3UploadService;

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
    public SlippiFile save(SlippiFile slippiFile) throws IOException {
        log.debug("Request to save SlippiFile : {}", slippiFile);
        populateHashValue(slippiFile);
        validateUniqueHash(slippiFile);
        s3UploadService.save(slippiFile);
        return slippiFileRepository.save(slippiFile);
    }

    public void populateHashValue(SlippiFile slippiFile) throws IOException {
        if(slippiFile.getHashValue() == null){
            MessageDigest dg = null;
            try {
                dg = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("Could not determine file identifier.");
            }

            byte[] digest = dg.digest(slippiFile.fileBytes());
            slippiFile.setHashValue(new String(Hex.encode(digest)));
        }
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

    //TODO
    public void validateUniqueHash(SlippiFile slippiFile){
        String hash = slippiFile.getHashValue();
        if(hash == null) {
            throw new IllegalArgumentException("Hash value not generated for file");
        }

    }
}
