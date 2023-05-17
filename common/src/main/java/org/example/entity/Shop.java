package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Shop extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private String logoImg;

    private boolean isCrawled;

    private boolean isDisplayed;

    private Long baseShippingFee;

    private Long jejuShippingFee;

    private Long islandShippingFee;

    private Long freeShippingFee;

    @Enumerated(EnumType.STRING)
    private ShopClassification shopClassification;

    @OneToMany(mappedBy = "shop")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<ShopCoupon> shopCoupon = new ArrayList<>();

    public void setIsCrawled(boolean isCrawled) {
        this.isCrawled = isCrawled;
    }
}
