package com.buildazan.utils;

import java.util.*;
import java.util.function.Supplier;

public class DefaultPageTemplates {

    public static List<Map<String, Object>> getTemplates() {
        return List.of(
            Map.of(
                "name", "Home",
                "slug", "home",
                "contentGenerator", (Supplier<Object>) DefaultPageTemplates::getHomePageContent
            ),
            Map.of(
                "name", "Product",
                "slug", "product",
                "contentGenerator", (Supplier<Object>) DefaultPageTemplates::getProductPageContent
            ),
            Map.of(
                "name", "About",
                "slug", "about", 
                "default", false,
                "contentGenerator", (Supplier<Object>) DefaultPageTemplates::getAboutPageContent
            )
        );
    }

    public static Map<String, List<Map<String, Object>>> getHomePageContent() {
        List<Map<String, Object>> globalContent = List.of(
            Map.of(
                "type", "topbar",
                "props", Map.of(
                    "messages", List.of(
                        Map.of("id", "msg1", "icon", "fas fa-truck", "text", "Free Worldwide Shipping on Orders Over $99"),
                        Map.of("id", "msg2", "icon", "fas fa-tag", "text", "Summer Sale: Up to 50% Off Selected Items"),
                        Map.of("id", "msg3", "icon", "fas fa-phone", "text", "24/7 Customer Support: 1-800-555-HELP")
                    ),
                    "backgroundColor", "#ffffff",
                    "textColor", "#000",
                    "fontSize", "0.9rem",
                    "fontWeight", "500",
                    "padding", "0.75rem 0",
                    "animationDuration", 12
                )
            ),
            Map.of(
                "type", "navbar",
                "props", Map.<String, Object>ofEntries(
                    Map.entry("styling", "default"),
                    Map.entry("background", "rgba(255, 255, 255, 0.95)"),
                    Map.entry("textColor", "#1a1a1a"),
                    Map.entry("logoType", "text"),
                    Map.entry("logoText", "Brand"),
                    Map.entry("logoImage", false),
                    Map.entry("links", List.of(
                        Map.of("label", "Home", "url", "/", "type", "internal", "target", "_self"),
                        Map.of("label", "Shop", "url", "/shop", "type", "internal", "target", "_self"),
                        Map.of("label", "Contact", "url", "/contact", "type", "internal", "target", "_self"),
                        Map.of("label", "About", "url", "/about", "type", "internal", "target", "_self")
                    )),
                    Map.entry("showSearch", true),
                    Map.entry("searchStyle", "compact"),
                    Map.entry("showAccount", true),
                    Map.entry("showCart", true),
                    Map.entry("cartCounter", true),
                    Map.entry("sticky", false),
                    Map.entry("logoFontSize", "1.8rem"),
                    Map.entry("logoColor", "#1a1a1a"),
                    Map.entry("navLinkFontSize", "1rem"),
                    Map.entry("navLinkFontWeight", "600"),
                    Map.entry("navLinkHoverColor", "#3b82f6"),
                    Map.entry("navLinkSpacing", "3rem"),
                    Map.entry("navAccentColor", "#ff4757"),
                    Map.entry("searchIconColor", "#1a1a1a"),
                    Map.entry("actionIconColor", "#1a1a1a")
                )
            ),
            Map.of(
                "type", "footer",
                "props", Map.of(
                    "backgroundColor", "#0a0a0a",
                    "textColor", "#ffffff",
                    "logoType", "text",
                    "logoText", "FASHION",
                    "logoImage", "",
                    "columns", List.of(
                        Map.of("title", "About Us", "links", List.of("Our Story", "Careers", "Sustainability", "Press")),
                        Map.of("title", "Customer Service", "links", List.of("Contact Us", "Shipping", "Returns", "FAQ")),
                        Map.of("title", "Social", "links", List.of("Instagram", "Twitter", "Facebook", "Pinterest"))
                    ),
                    "showNewsletter", true,
                    "socialIcons", true,
                    "paymentIcons", true,
                    "copyrightText", "© 2024 FASHION. All rights reserved."
                )
            )
        );
    
        List<Map<String, Object>> content = List.of(
            Map.of(
                "type", "hero",
                "props", Map.<String, Object>ofEntries(
                    Map.entry("slides", List.of(
                        Map.of(
                            "backgroundType", "image",
                            "background", "https://pipeline-theme-fashion.myshopify.com/cdn/shop/files/clothes-spr21-51_97fa03cf-5481-420d-abb6-3e85014d47aa.jpg?v=1726092789&width=1920",
                            "title", "Elevate Your Lifestyle",
                            "subtitle", "Premium essentials for modern living. Experience quality like never before.",
                            "ctaButtons", List.of(
                                Map.of("text", "Shop Now", "variant", "primary"),
                                Map.of("text", "Learn More", "variant", "secondary")
                            )
                        ),
                        Map.of(
                            "backgroundType", "image",
                            "background", "https://images.unsplash.com/photo-1543304216-b46be324b571?q=80&w=1581&auto=format&fit=crop",
                            "title", "Discover New Trends",
                            "subtitle", "Stay ahead of the curve with our exclusive collections.",
                            "ctaButtons", List.of(
                                Map.of("text", "Explore", "variant", "primary")
                            )
                        )
                    )),
                    Map.entry("titleFontSize", "3.5rem"),
                    Map.entry("titleColor", "#fff"),
                    Map.entry("subtitleFontSize", "1.5rem"),
                    Map.entry("subtitleColor", "#dfe1e4"),
                    Map.entry("ctaPrimaryBackground", "#fff"),
                    Map.entry("ctaPrimaryColor", "#111"),
                    Map.entry("ctaSecondaryBackground", "transparent"),
                    Map.entry("ctaSecondaryColor", "#fff"),
                    Map.entry("ctaSecondaryBorder", "2px solid rgba(255,255,255,0.7)"),
                    Map.entry("heroOverlay", "linear-gradient(to bottom, rgba(15, 15, 26, 0.151) 0%, rgba(15, 15, 26, 0.164) 50%, rgba(15, 15, 26, 0.178) 100%)"),
                    Map.entry("autoPlay", true),
                    Map.entry("autoplayDelay", 5000),
                    Map.entry("autoplayDisableOnInteraction", false),
                    Map.entry("loop", true),
                    Map.entry("navigation", true),
                    Map.entry("paginationClickable", true)
                )
            ),
            Map.of(
                "type", "productGrid",
                "props", Map.<String, Object>ofEntries(
                    Map.entry("autoPlay", true),
                    Map.entry("autoplayDelay", 5000),
                    Map.entry("autoplayDisableOnInteraction", false),
                    Map.entry("loop", true),
                    Map.entry("navigation", true),
                    Map.entry("pagination", true),
                    Map.entry("paginationClickable", true),
                    Map.entry("paginationType", "bullets"),
                    Map.entry("productCollection", "latest"),
                    Map.entry("numberOfProducts", 6),
                    Map.entry("desktopColumns", 4),
                    Map.entry("tabletColumns", 2),
                    Map.entry("mobileColumns", 1),
                    Map.entry("sectionTitle", "Featured Products"),
                    Map.entry("sectionTitleColor", "#1a1a2c"),
                    Map.entry("sectionTitleFontSize", "2.5rem"),
                    Map.entry("cardBackground", "#ffffff"),
                    Map.entry("cardBorderRadius", "12px"),
                    Map.entry("cardBoxShadow", "0 4px 24px rgba(0,0,0,0.08)"),
                    Map.entry("productImageObjectFit", "cover"),
                    Map.entry("productImageBackgroundPosition", "center"),
                    Map.entry("productImageBorderRadius", "0px"),
                    Map.entry("titleColor", "#1a1a1a"),
                    Map.entry("titleFontSize", "1.1rem"),
                    Map.entry("titleFontWeight", "600"),
                    Map.entry("currentPriceColor", "#1a1a1a"),
                    Map.entry("currentPriceFontSize", "1.25rem"),
                    Map.entry("originalPriceColor", "#999"),
                    Map.entry("originalPriceFontSize", "0.9rem"),
                    Map.entry("ratingStarSize", "0.9rem"),
                    Map.entry("ratingFilledColor", "#ffd700"),
                    Map.entry("ratingEmptyColor", "#e0e0e0"),
                    Map.entry("buyButtonBackground", "#1a1a1a"),
                    Map.entry("buyButtonTextColor", "#fff"),
                    Map.entry("buyButtonFontSize", "1rem"),
                    Map.entry("buyButtonBorderRadius", "8px"),
                    Map.entry("cartButtonBackground", "#ffffff"),
                    Map.entry("cartButtonColor", "#000"),
                    Map.entry("cartButtonHoverColor", "#ff4d4f"),
                    Map.entry("saleBadgeBackground", "#ff4d4f"),
                    Map.entry("saleBadgeTextColor", "#fff"),
                    Map.entry("saleBadgeFontSize", "0.8rem"),
                    Map.entry("saleBadgePadding", "0.25rem 1rem"),
                    Map.entry("saleBadgeBorderRadius", "20px")
                )
            ),
            Map.of(
                "type", "testimonials",
                "props", Map.<String, Object>ofEntries(
                    Map.entry("testimonials", List.of(
                        Map.of(
                            "id", 2342,
                            "name", "Sarah Johnson",
                            "role", "Fashion Blogger",
                            "text", "The quality and style are unmatched. My go-to for seasonal trends!",
                            "rating", 5,
                            "image", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80"
                        ),
                        Map.of(
                            "id", 2345,
                            "name", "Michael Chen",
                            "role", "Stylist",
                            "text", "Perfect balance of comfort and style. My clients love these pieces.",
                            "rating", 4,
                            "image", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
                        )
                    )),
                    Map.entry("columns", 2),
                    Map.entry("cardBackground", "#ffffff"),
                    Map.entry("cardBorderRadius", "15px"),
                    Map.entry("cardBoxShadow", "0 10px 30px rgba(0,0,0,0.05)"),
                    Map.entry("headerTitle", "What Our Clients Say"),
                    Map.entry("headerTitleColor", "#222222"),
                    Map.entry("headerTitleFontSize", "2.5rem"),
                    Map.entry("headerDescription", "Discover what our customers say about their experience"),
                    Map.entry("headerDescriptionColor", "#666"),
                    Map.entry("headerDescriptionFontSize", "1.1rem"),
                    Map.entry("textColor", "#2d2d2d"),
                    Map.entry("accentColor", "#e63946"),
                    Map.entry("nameFontSize", "1.2rem"),
                    Map.entry("nameColor", "#2d2d2d"),
                    Map.entry("roleFontSize", "0.9rem"),
                    Map.entry("roleColor", "#666"),
                    Map.entry("ratingStarSize", "20px"),
                    Map.entry("ratingFilledColor", "#ffd700"),
                    Map.entry("ratingEmptyColor", "#e0e0e0"),
                    Map.entry("showRating", true),
                    Map.entry("showProfileImage", true)
                )
            ),
            Map.of(
                "type", "contactForm",
                "props", Map.<String, Object>ofEntries(
                    Map.entry("companyName", "Fashion Co."),
                    Map.entry("companyAddress", "123 Style Street\nNew York, NY 10001"),
                    Map.entry("phoneNumber", "+1 (555) 123-4567"),
                    Map.entry("emailAddress", "hello@fashionco.com"),
                    Map.entry("showInstagram", true),
                    Map.entry("instagramLink", "https://instagram.com/fashionco"),
                    Map.entry("showTwitter", true),
                    Map.entry("twitterLink", "https://twitter.com/fashionco"),
                    Map.entry("showFacebook", true),
                    Map.entry("facebookLink", "https://facebook.com/fashionco"),
                    Map.entry("showPinterest", true),
                    Map.entry("pinterestLink", "https://pinterest.com/fashionco"),
                    Map.entry("headerTitle", "Get in Touch"),
                    Map.entry("headerDescription", "We'd love to hear from you! Reach out for inquiries, collaborations, or just to say hello."),
                    Map.entry("inputBorderColor", "#e0e0e0"),
                    Map.entry("inputFocusBorderColor", "#1D4ED8"),
                    Map.entry("labelColor", "#666"),
                    Map.entry("labelFocusColor", "#1D4ED8"),
                    Map.entry("submitButtonBackground", "#1D4ED8"),
                    Map.entry("submitButtonTextColor", "#ffffff"),
                    Map.entry("submitButtonFontSize", "1.1rem"),
                    Map.entry("successMessageTitle", "Message Sent Successfully!"),
                    Map.entry("successMessageBody", "We'll get back to you within 24 hours"),
                    Map.entry("infoTitle", "Contact Information"),
                    Map.entry("infoTitleColor", "#222222"),
                    Map.entry("infoTextColor", "#4A4A4A"),
                    Map.entry("socialIconColor", "rgb(120, 120, 120)"),
                    Map.entry("socialIconHoverColor", "#1D4ED8")
                )
            )
        );
    
        return Map.of("globalContent", globalContent, "content", content);
    }
    public static List<Map<String, Object>> getProductPageContent() {
        return List.of(
            Map.of("type", "product")
        );
    };
    public static List<Map<String, Object>> getAboutPageContent() {
        return List.of(
            Map.of("type", "aboutPage")
        );
    };
}
