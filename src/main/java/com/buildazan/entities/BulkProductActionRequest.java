package com.buildazan.entities;

import java.util.List;

public class BulkProductActionRequest {
    private List<String> productIds;
    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
    
}
    