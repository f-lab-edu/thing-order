package org.example.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
