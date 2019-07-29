package com.rez.melee.slippi.service;

import com.rez.melee.slippi.domain.SlippiFile;

import java.io.IOException;

public interface S3UploadService {

    void save(SlippiFile file) throws IOException;
}
