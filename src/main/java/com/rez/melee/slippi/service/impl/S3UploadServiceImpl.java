package com.rez.melee.slippi.service.impl;

import com.rez.melee.slippi.domain.SlippiFile;
import com.rez.melee.slippi.service.S3UploadService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class S3UploadServiceImpl implements S3UploadService {

    @Override
    public void save(SlippiFile file) throws IOException {
        //TODO
    }
}
