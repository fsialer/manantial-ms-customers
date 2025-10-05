package com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage.facade;

import org.springframework.stereotype.Component;

@Component
public class AzureBlobStoreStorageDrive implements StorageDrive{

    @Override
    public byte[] getFile(String path) {
        return new byte[0];
    }
}
