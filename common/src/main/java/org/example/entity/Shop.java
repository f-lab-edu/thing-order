package org.example.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
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

    public Shop(String name, boolean isDisplayed) {
        this.name = name;
        this.isDisplayed = isDisplayed;
    }
}
