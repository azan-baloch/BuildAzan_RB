package com.buildazan.repo;

import java.util.List;
import java.util.Map;

import com.buildazan.projection.SlugProjection;

public interface StoreCustomRepo {
    void updateStoreGeneralDetails(Map<String, String> details);
    void udpateStoreDomain(Map<String, String> payload);
}
