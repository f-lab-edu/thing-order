package org.example.message;

import lombok.Getter;
import org.example.entity.OptionsType;

@Getter
public class DeleteCartItemMessage {
    private final Long productId;

    private final OptionsType optionsType;

    private final Long optionId;

    public DeleteCartItemMessage(Long productId, OptionsType optionsType, Long optionId) {
        if (productId == null) {
            throw new IllegalArgumentException("product id can not be null");
        }

        if (optionsType == OptionsType.Combination && optionId == null) {
            throw new IllegalArgumentException("Combination option must contain optionId");
        }

        this.productId = productId;
        this.optionsType = optionsType;
        this.optionId = optionId;
    }
}
