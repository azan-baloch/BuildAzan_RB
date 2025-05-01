package com.buildazan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.repo.CategoryRepo;
import com.buildazan.repo.OrderRepo;
import com.buildazan.repo.ProductRepo;

@Service
public class DashboardService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    public Map<String, Object> dashboardStats(String storeId){
        Long ordersCount =  orderRepo.countByStoreId(storeId);
        Long productsCount = productRepo.countByStoreId(storeId);
        Long categoriesCount = categoryRepo.countByStoreId(storeId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("ordersCount", ordersCount);
        stats.put("productsCount", productsCount);
        stats.put("categoriesCount", categoriesCount);
        stats.put("totalIncome", orderRepo.getTotalIncomeByStoreId(storeId));
        return stats;
    }
}
