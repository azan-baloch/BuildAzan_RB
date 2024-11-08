package com.buildazan.projection;

import java.time.LocalDateTime;
import java.util.List;
public interface ProductProjection {
    String getId();
    String getStoreId();
    String getName();
    Double getPrice();
    Double getDiscountPrice();
    LocalDateTime getCreatedDate();
    Boolean getTrackInventory();
    int getStockQuantity();
    Boolean getStockStatus();
    String getProductImage();
    String getStatus();
    List<String> getCategoryId();
}
