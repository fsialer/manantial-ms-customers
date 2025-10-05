package com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage;

import com.fernando.manantial_ms_customers.application.ports.output.StorageOutputPort;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage.facade.StorageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageAdapter implements StorageOutputPort {
    @Value("${storage.type}")
    private String storageType;
    private final StorageFactory storageFactory;

    @Override
    public byte[] getFile(String path) {
        return storageFactory.getStorageDrive(storageType).getFile(path);
    }
}
