package com.buildazan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.repo.OrderRepo;
import com.buildazan.repo.ProductRepo;

@Service
public class DashboardService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;
    public Map<String, Long> dashboardStats(String storeId){
        Long ordersCount =  orderRepo.count();
        Long productsCount = productRepo.count();
        Map<String, Long> stats = new HashMap<>();
        stats.put("ordersCount", ordersCount);
        stats.put("productsCount", productsCount);
        return stats;
    }
}
