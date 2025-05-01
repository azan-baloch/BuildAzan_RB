package com.buildazan.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
    Page<Order> findAllByStoreId(String storeId, Pageable pageable);
    long countByStoreId(String storeId);
    @Aggregation(pipeline = {
        "{ $match: { storeId: ?0 } }",
        "{ $group: { _id: null, totalIncome: { $sum: '$totalPrice' } } }"
    })
    Double getTotalIncomeByStoreId(String storeId);
}
