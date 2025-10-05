package com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
@Slf4j
public class LocalStorageDrive implements StorageDrive{

    @Override
    public byte[] getFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new RuntimeException("File don't exists: " + path);
            }
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from local storage", e);
        }
    }
}
