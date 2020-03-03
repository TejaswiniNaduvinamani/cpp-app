package com.gfs.cpp.proxy.clm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileRemover {
    public static final Logger logger = LoggerFactory.getLogger(FileRemover.class);

    public boolean deleteFile(File file) {
        Boolean isDeleted = false;
        try {
            isDeleted = Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            logger.info("Unable to delete - {}", file.getName());
        }
        return isDeleted;
    }
}
