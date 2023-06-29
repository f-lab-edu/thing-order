package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CartItemOption {
    private long optionId;

    private String legacyOptionId;

    private String optionName1;

    private String optionValue1;

    private String optionName2;

    private String optionValue2;

    private String optionName3;

    private String optionValue3;

    private Long optionPrice;

    public CartItemOption(long optionId, String legacyOptionId, String optionName1, String optionValue1,
        String optionName2,
        String optionValue2, String optionName3, String optionValue3, Long optionPrice) {
        this.optionId = optionId;
        this.legacyOptionId = legacyOptionId;
        this.optionName1 = optionName1;
        this.optionValue1 = optionValue1;
        this.optionName2 = optionName2;
        this.optionValue2 = optionValue2;
        this.optionName3 = optionName3;
        this.optionValue3 = optionValue3;
        this.optionPrice = optionPrice;
    }
}
