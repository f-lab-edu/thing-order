package org.example.entity;

import lombok.Getter;

@Getter
public class OrderItemOption {

    private Long optionId;

    private String optionName1;

    private String optionValue1;

    private String optionName2;

    private String optionValue2;

    private String optionName3;

    private String optionValue3;

    private Long optionPrice;

    private Long orderQuality;

    public OrderItemOption(Long optionId, String optionName1, String optionValue1,
            String optionName2,
            String optionValue2, String optionName3, String optionValue3, Long optionPrice,
            Long orderQuality) {
        this.optionId = optionId;
        this.optionName1 = optionName1;
        this.optionValue1 = optionValue1;
        this.optionName2 = optionName2;
        this.optionValue2 = optionValue2;
        this.optionName3 = optionName3;
        this.optionValue3 = optionValue3;
        this.optionPrice = optionPrice;
        this.orderQuality = orderQuality;
    }

    public OrderItemOption(Long orderQuality) {
        this.orderQuality = orderQuality;
    }
}
