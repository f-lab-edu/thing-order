package org.example.dto.order;

import org.example.entity.OptionsType;
import org.example.entity.Product;
import org.example.exception.GraphqlException;

import lombok.Getter;

@Getter
public class ProductStockCountMessage {

    private final Product product;

    private final Long optionId;

    private final Long stock;

    public ProductStockCountMessage(Product product, Long optionId, Long stock) {
        if (product.getOptionsType() == OptionsType.Combination && optionId == null) {
            throw new GraphqlException("combination product's optoin id can not be null");
        }

        this.product = product;
        this.optionId = optionId;
        this.stock = stock;
    }
}
