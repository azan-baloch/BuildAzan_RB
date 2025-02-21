package com.buildazan.utils;

import java.util.*;
import java.util.function.Supplier;

public class DefaultPageTemplates {

    public static List<Map<String, Object>> getTemplates() {
        return List.of(
                Map.of("name", "Home",
                        "slug", "home",
                        "contentGenerator",
                        (Supplier<List<Map<String, Object>>>) DefaultPageTemplates::getHomePageContent),
                Map.of("name", "Product",
                        "slug", "product",
                        "contentGenerator",
                        (Supplier<List<Map<String, Object>>>) DefaultPageTemplates::getProductPageContent),
                Map.of("name", "About",
                        "slug", "about",
                        "default", false,
                        "contentGenerator",
                        (Supplier<List<Map<String, Object>>>) DefaultPageTemplates::getProductPageContent)
                        );

    };

    public static List<Map<String, Object>> getHomePageContent() {
        return List.of(Map.of("type", "topbar"), Map.of("type", "navbar", "variant", "nav1", "navStyles", "default",
                "showButton", true, "logoType", "image", "showSearch", true, "showAccount", true, "showCart", true));
    };

    public static List<Map<String, Object>> getProductPageContent(){
        return List.of(Map.of("type", "product"));
    }

}
