package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Shop {
    private Long id;
    private String name;
    private String description;
    private String logoImg;
    private boolean isCrawled;
    private List<Product> products;
}
