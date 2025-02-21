package com.buildazan.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
    Page<Order> findAllByStoreId(String storeId, Pageable pageable);
}
