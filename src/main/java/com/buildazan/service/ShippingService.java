package com.buildazan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buildazan.entities.ConditionalFreeShipping;
import com.buildazan.entities.CustomRateRule;
import com.buildazan.entities.CustomRateShipping;
import com.buildazan.entities.FlatRateShipping;
import com.buildazan.entities.FreeShipping;
import com.buildazan.entities.PriceRange;
import com.buildazan.entities.ShippingOption;
import com.buildazan.enums.ShippingType;
import com.buildazan.repo.ShippingOptionRepo;

@Service
public class ShippingService {
    @Autowired
    private ShippingOptionRepo shippingRepo; 

    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<ShippingOption> getShippings(){
        return shippingRepo.findAll();
    }

    public Optional<ShippingOption> findShippingById(String id){
        return shippingRepo.findById(id);
    }

    @Transactional
    public void updateShippingOption(String id, boolean enabled) {
        if (enabled) {
            // Disable all other options
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").ne(id));
            Update update = new Update();
            update.set("enabled", false);
            mongoTemplate.updateMulti(query, update, ShippingOption.class);

            // Enable the selected option
            updateEnabledStatus(id, true);
        } else {
            // Disable the selected option
            updateEnabledStatus(id, false);
        }
    }

    @Transactional
    public ShippingOption updateShippingOptionDetails(ShippingOption existingOption, Map<String, Object> shippingDetails) {
        existingOption.setName((String) shippingDetails.get("name"));
        existingOption.setDescription((String) shippingDetails.get("description"));
        existingOption.setMinEstimatedDays(Integer.parseInt((String) shippingDetails.get("minEstimatedDays")));
        existingOption.setMaxEstimatedDays(Integer.parseInt((String) shippingDetails.get("maxEstimatedDays")));
        existingOption.setShippingRegion((String) shippingDetails.get("region"));
        existingOption.setShippingMethod((String) shippingDetails.get("shippingMethod"));

        if (existingOption instanceof FreeShipping) {
            // No additional fields to update
        } else if (existingOption instanceof FlatRateShipping) {
            ((FlatRateShipping) existingOption).setFlatRate(Double.parseDouble((String) shippingDetails.get("flatRate")));
        } else if (existingOption instanceof ConditionalFreeShipping) {
            List<Map<String, Object>> priceRangeMaps = (List<Map<String, Object>>) shippingDetails.get("priceRanges");
            List<PriceRange> priceRanges = priceRangeMaps.stream()
                    .map(this::convertToPriceRange)
                    .collect(Collectors.toList());
            ((ConditionalFreeShipping) existingOption).setPriceRanges(priceRanges);
        } else if (existingOption instanceof CustomRateShipping) {
            List<Map<String, Object>> customRulesMaps = (List<Map<String, Object>>) shippingDetails.get("customRules");
            List<CustomRateRule> customRateRules = customRulesMaps.stream()
                    .map(this::convertToCustomRateRule)
                    .collect(Collectors.toList());
            ((CustomRateShipping) existingOption).setCustomRateRules(customRateRules);
        }

        return shippingRepo.save(existingOption);
    }


    private void updateEnabledStatus(String id, boolean enabled) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("enabled", enabled);
        mongoTemplate.updateFirst(query, update, ShippingOption.class);
    }

    @Transactional
    public void deleteShippingOption(String id) {
        shippingRepo.deleteById(id);
    }


    public ShippingOption createShippingOption(Map<String, Object> shippingDetails){
        // String shippingType = (String) shippingDetails.get("shippingType");
        String shippingType = "LOCAL";
        String shippingMethod = (String) shippingDetails.get("shippingMethod");
        ShippingOption shippingOption = null;
        switch (shippingMethod) {
            case "FreeShipping":
                shippingOption = new FreeShipping();
                break;
            case "ConditionalFreeShipping":
                shippingOption = new ConditionalFreeShipping();
                List<Map<String, Object>> priceRangeMaps = (List<Map<String, Object>>) shippingDetails.get("priceRanges");
                List<PriceRange> priceRanges = priceRangeMaps.stream() 
                .map(this::convertToPriceRange)
                .collect(Collectors.toList());
                ((ConditionalFreeShipping) shippingOption).setPriceRanges(priceRanges);
                break;
            case "FlatRateShipping":
                shippingOption = new FlatRateShipping();
                ((FlatRateShipping) shippingOption).setFlatRate(Double.parseDouble((String) shippingDetails.get("flatRate")));
                break;
            case "CustomRateShipping":
                shippingOption = new CustomRateShipping();  
                List<Map<String, Object>> customeRulesMaps = (List<Map<String, Object>>) shippingDetails.get("customRules");
                List<CustomRateRule> customRateRules = customeRulesMaps.stream()
                .map(this::convertToCustomRateRule)
                .collect(Collectors.toList());
                ((CustomRateShipping) shippingOption).setCustomRateRules(customRateRules);
            break;
        }
        if (shippingOption!=null) {
            shippingOption.setName((String) shippingDetails.get("name"));
            shippingOption.setDescription((String) shippingDetails.get("description"));
            shippingOption.setMinEstimatedDays(Integer.parseInt((String) shippingDetails.get("minEstimatedDays")));
            shippingOption.setMaxEstimatedDays(Integer.parseInt((String) shippingDetails.get("maxEstimatedDays")));
            shippingOption.setShippingRegion((String) shippingDetails.get("shippingRegion"));
            shippingOption.setShippingType(ShippingType.valueOf(shippingType));
            shippingOption.setEnabled(false);
            shippingOption.setShippingMethod(shippingMethod);
        }
        return shippingRepo.save(shippingOption);
    }

    private PriceRange convertToPriceRange(Map<String, Object> shippingDetails) {
        PriceRange priceRange = new PriceRange();
        priceRange.setMinPrice(Double.parseDouble((String) shippingDetails.get("minPrice")));
        priceRange.setMaxPrice(Double.parseDouble((String) shippingDetails.get("maxPrice")));
        priceRange.setShippingFee(Double.parseDouble((String) shippingDetails.get("shippingFee")));
        return priceRange;
    }

    private CustomRateRule convertToCustomRateRule(Map<String, Object> shippingDetails){
        CustomRateRule customRateRule = new CustomRateRule();
        customRateRule.setMinPrice(Double.parseDouble((String) shippingDetails.get("minPrice")));
        customRateRule.setMaxPrice(Double.parseDouble((String) shippingDetails.get("maxPrice")));
        if (shippingDetails.containsKey("minWeight") && shippingDetails.get("minWeight") != null) {
            customRateRule.setMinWeight(Double.parseDouble((String) shippingDetails.get("minWeight")));
        } else {
            customRateRule.setMinWeight(0);
        }
    
        if (shippingDetails.containsKey("maxWeight") && shippingDetails.get("maxWeight") != null) {
            customRateRule.setMaxWeight(Double.parseDouble((String) shippingDetails.get("maxWeight")));
        } else {
            customRateRule.setMaxWeight(0); // or some default value
        }
        customRateRule.setShippingFee(Double.parseDouble((String) shippingDetails.get("shippingFee")));
        return customRateRule;
    }

}
