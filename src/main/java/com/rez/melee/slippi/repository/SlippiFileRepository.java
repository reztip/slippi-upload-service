package com.rez.melee.slippi.repository;

import com.rez.melee.slippi.domain.SlippiFile;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the SlippiFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlippiFileRepository extends MongoRepository<SlippiFile, String> {

}
