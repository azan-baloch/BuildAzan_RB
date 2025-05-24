package com.buildazan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroup {
    private String id;
    private String name;  // "Color", "Size" etc
    private String type;  // "color", "select", "button"
    private List<String> values; // ["red", "blue"] or ["#ff0000", "#00ff00"]
}