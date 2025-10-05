package com.fernando.manantial_ms_customers.infrastructure.adapters.output.storage.facade;

public interface StorageDrive {
    byte[] getFile(String path);
}
