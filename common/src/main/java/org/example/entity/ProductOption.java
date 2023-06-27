package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ProductOption {

    private long optionId;

    private String optionName1;

    private String optionValue1;

    private String optionName2;

    private String optionValue2;

    private String optionName3;

    private String optionValue3;

    private Boolean isAvailable;

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

    public ProductOption(long optionId, String optionName1, String optionValue1, String optionName2,
        String optionValue2, String optionName3, String optionValue3, Long stockCount, Long optionPrice) {
        this.optionId = optionId;
        this.optionName1 = optionName1;
        this.optionValue1 = optionValue1;
        this.optionName2 = optionName2;
        this.optionValue2 = optionValue2;
        this.optionName3 = optionName3;
        this.optionValue3 = optionValue3;
        this.stockCount = stockCount;
        this.statusOfStock = StatusOfStock.OnSale;
        this.optionPrice = optionPrice;
        this.isAvailable = true;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public void setStatusOfStock(StatusOfStock statusOfStock) {
        this.statusOfStock = statusOfStock;
    }
}
