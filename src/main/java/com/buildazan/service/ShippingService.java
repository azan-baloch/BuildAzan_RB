package com.buildazan.service;

import java.util.ArrayList;
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
import com.buildazan.repo.ShippingOptionRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ShippingService {
    @Autowired
    private ShippingOptionRepo shippingRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ShippingOption> findAllShippingsByStoreId(String storeId) {
        return shippingRepo.findAllByStoreId(storeId);
    }

    public Optional<ShippingOption> findShippingById(String id) {
        return shippingRepo.findById(id);
    }

    public ShippingOption findByStoreIdAndEnabled(String storeId){
        return shippingRepo.findByStoreIdAndEnabled(storeId);
    }

    public void updateShippingStatus(Map<String, String> payload) {
        String shippingOptionId = payload.get("shippingOptionId");
        String storeId = payload.get("storeId");
        Boolean enabled = Boolean.parseBoolean(payload.get("enabled"));

        if (enabled) {
            Query disableAllQuery = new Query(Criteria.where("storeId").is(storeId));
            Update disableAllUpdate = new Update().set("enabled", false);
            mongoTemplate.updateMulti(disableAllQuery, disableAllUpdate, ShippingOption.class);

            // Step 2: Enable the selected shipping option
            Query enableQuery = new Query(Criteria.where("_id").is(shippingOptionId));
            Update enableUpdate = new Update().set("enabled", true);
            mongoTemplate.updateFirst(enableQuery, enableUpdate, ShippingOption.class);
        } else {
            Query disableQuery = new Query(Criteria.where("_id").is(shippingOptionId));
            Update disableUpdate = new Update().set("enabled", false);
            mongoTemplate.updateFirst(disableQuery, disableUpdate, ShippingOption.class);
        }
    }

    public void deleteShippingOption(String id) {
        shippingRepo.deleteById(id);
    }

    @Transactional
    public void updateShippingOption(Map<String, String> shippingDetails) {
        String id = shippingDetails.get("id");
        String shippingType = shippingDetails.get("shippingType");

        Update update = new Update()
                .set("name", shippingDetails.get("name"))
                .set("description", shippingDetails.get("description"))
                .set("minEstimatedDays", Integer.parseInt(shippingDetails.get("minEstimatedDays")))
                .set("maxEstimatedDays", Integer.parseInt(shippingDetails.get("maxEstimatedDays")))
                .set("shippingRegion", shippingDetails.get("shippingRegion"));

        switch (shippingType) {
            case "conditional-free-shipping":
                List<PriceRange> priceRanges = getPriceRangesFromDetails(shippingDetails);
                update.set("priceRanges", priceRanges);
                break;

            case "flat-rate-shipping":
                update.set("flatRate", Double.parseDouble(shippingDetails.get("flatRate")));
                break;

            case "custom-shipping":
                List<CustomRateRule> customRateRules = getCustomRateRulesFromDetails(shippingDetails);
                update.set("customRateRules", customRateRules);
                break;

            case "free-shipping":
            default:
                break;
        }

        Query query = new Query(Criteria.where("_id").is(id).and("storeId").is(shippingDetails.get("storeId")));
        mongoTemplate.updateFirst(query, update, ShippingOption.class);
    }

    @Transactional
    public ShippingOption createShippingOption(Map<String, String> shippingDetails) {
        String shippingType = shippingDetails.get("shippingType");
        ShippingOption shippingOption = null;

        switch (shippingType) {
            case "free-shipping":
                shippingOption = new FreeShipping();
                break;

            case "conditional-free-shipping":
                shippingOption = new ConditionalFreeShipping();
                List<PriceRange> priceRanges = getPriceRangesFromDetails(shippingDetails);
                ((ConditionalFreeShipping) shippingOption).setPriceRanges(priceRanges);
                break;

            case "flat-rate-shipping":
                shippingOption = new FlatRateShipping();
                ((FlatRateShipping) shippingOption).setFlatRate(Double.parseDouble(shippingDetails.get("flatRate")));
                break;

            case "custom-shipping":
                shippingOption = new CustomRateShipping();
                List<CustomRateRule> customRateRules = getCustomRateRulesFromDetails(shippingDetails);
                ((CustomRateShipping) shippingOption).setCustomRateRules(customRateRules);
                break;

            default:
                throw new IllegalArgumentException("Invalid shipping type");
        }

        if (shippingOption != null) {
            shippingOption.setStoreId(shippingDetails.get("storeId"));
            shippingOption.setName(shippingDetails.get("name"));
            shippingOption.setDescription(shippingDetails.get("description"));
            shippingOption.setMinEstimatedDays(Integer.parseInt(shippingDetails.get("minEstimatedDays")));
            shippingOption.setMaxEstimatedDays(Integer.parseInt(shippingDetails.get("maxEstimatedDays")));
            shippingOption.setShippingRegion(shippingDetails.get("shippingRegion"));
            shippingOption.setEnabled(true);
            shippingOption.setShippingType(shippingType);
            Query query = new Query(
                    Criteria.where("storeId").is(shippingDetails.get("storeId")).and("enabled").is(true));
            Update update = new Update().set("enabled", false);
            mongoTemplate.updateMulti(query, update, ShippingOption.class);
        }

        return shippingRepo.save(shippingOption);
    }

    private List<PriceRange> getPriceRangesFromDetails(Map<String, String> shippingDetails) {
        List<PriceRange> priceRanges = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String priceRangesJson = (String) shippingDetails.get("priceRanges");

            if (priceRangesJson != null && !priceRangesJson.isEmpty()) {
                priceRanges = objectMapper.readValue(priceRangesJson, new TypeReference<List<PriceRange>>() {
                });
            } else {
                System.out.println("No price ranges provided.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return priceRanges;
    }

    private List<CustomRateRule> getCustomRateRulesFromDetails(Map<String, String> shippingDetails) {
        List<CustomRateRule> customRateRules = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String customRatesJson = (String) shippingDetails.get("customRates");

            if (customRatesJson != null && !customRatesJson.isEmpty()) {
                customRateRules = objectMapper.readValue(customRatesJson, new TypeReference<List<CustomRateRule>>() {
                });
            } else {
                System.out.println("No custom rates provided.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customRateRules;
    }

}
