package com.buildazan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Order;
import com.buildazan.repo.OrderRepo;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createOrder(Order order){
        orderRepo.save(order);
    }

    public Page<Order> getOrders(String storeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepo.findAllByStoreId(storeId, pageable);
    }

    public void updateOrder(String orderId, String paymentStatus, String shippingStatus){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(orderId));
        Update update = new Update();
        update.set("paymentStatus", paymentStatus);
        update.set("shippingStatus", shippingStatus);
        mongoTemplate.updateFirst(query, update, Order.class);
    }
}
