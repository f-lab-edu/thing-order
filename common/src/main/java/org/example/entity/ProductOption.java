package org.example.entity;

import lombok.Getter;

@Getter
public class ProductOption {

    private long optionId;

    private String optionName1;

    private String optionValue1;

    private String optionName2;

    private String optionValue2;

    private String optionName3;

    private String optionValue3;

    private boolean isAvailable;

    private Long stockCount;

    private Long optionPrice;

    private String adminCode;

    private StatusOfStock statusOfStock;

    public ProductOption(long optionId, String optionName1, String optionValue1, Long stockCount,
            StatusOfStock statusOfStock) {
        this.optionId = optionId;
        this.optionName1 = optionName1;
        this.optionValue1 = optionValue1;
        this.stockCount = stockCount;
        this.statusOfStock = statusOfStock;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }
}
