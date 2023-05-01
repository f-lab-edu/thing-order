package org.example.domain;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Product {
    private Long id;
    private String coverImg;
    private Shop shop;
    private String name;
    private DiscountType discountType;
    private Long discountRate;
    private Long discountPrice;
    private Long price;
    private boolean isFreeShipping;
    private ProductClassification productClassification;
    private ShippingType shippingType;
    private Long baseShippingFee;
    private Long jejuShippingFee;
    private Long freeShippingFee;
    private Long islandShippingFee;
    private LocalDateTime estimatedWareHousingDate;
    private LocalDateTime preReservedEndTime;
    private EstimatedWarehousingDateType estimatedWarehousingDateType;
    private boolean isCrawled;
    private String crawlLink;
    private Long stockCount;
    private OptionsType optionsType;
    private ProductOption productOption;

    public void setIsFreeShipping(boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    public void setIsCrawled(boolean isCrawled) {
        this.isCrawled = isCrawled;
    }
}
