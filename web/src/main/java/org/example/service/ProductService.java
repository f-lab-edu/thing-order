package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateOrderItemRequest;
import org.example.entity.OptionsType;
import org.example.entity.Product;
import org.example.entity.ProductOption;
import org.example.exception.GraphqlException;
import org.example.message.ProductStockCountMessage;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void checkProductExist(List<Long> productIdsToOrder) {
        for (Long productId : productIdsToOrder) {
            Optional<Product> product = productRepository.findById(productId);
            product.orElseThrow(() -> new GraphqlException("Could not find the product with ID"));
        }
    }

    public void checkProductStockCount(List<CreateOrderItemRequest> itemsToOrder) {
        List<Product> lackOfStockCountProducts = new ArrayList<Product>();

        for (CreateOrderItemRequest item : itemsToOrder) {
            Optional<Product> product = productRepository.findById(item.getProductId());
            product.ifPresent(el -> {
                if (el.getStockCount() < item.getOrderQuantity()) {
                    lackOfStockCountProducts.add(el);
                }
            });
        }

        List<Product> lackOfOptionStockCountProducts = new ArrayList<Product>();

        for (CreateOrderItemRequest item : itemsToOrder) {
            Optional<Product> product = productRepository.findById(item.getProductId());

            if (product.isPresent()) {
                Product p = product.get();
                if (p.getOptionsType() != OptionsType.Combination) {
                    continue;
                }

                for (ProductOption productOption : p.getOptions()) {
                    if (item.getOptionId() == productOption.getOptionId()
                            && productOption.getStatusOfStock() != null) {
                        productOption.setStockCount(
                                productOption.getStockCount() - item.getOrderQuantity());

                        if (productOption.getStockCount() < 0 && lackOfStockCountProducts.stream()
                                .noneMatch(el -> el.getId() == p.getId())) {
                            lackOfOptionStockCountProducts.add(p);
                        }
                    }
                }
            }
        }

        if (lackOfStockCountProducts.size() > 0 || lackOfOptionStockCountProducts.size() > 0) {
            HashMap<String, Object> extensions = new HashMap<>();
            extensions.put("code", "LACK_OF_STOCK_COUNT");
            List<String> messages = new ArrayList<>();
            for (Product p : lackOfStockCountProducts) {
                messages.add(p.getName());
            }
            for (Product p : lackOfOptionStockCountProducts) {
                messages.add(p.getName() + " / 옵션 재고 부족");
            }
            extensions.put("soldOutProductName", messages);

            throw new GraphqlException("stock count less than order quantity", extensions);
        }
    }

    public Product findProductByDisplayedAndShopDisplayed(Long productId) {
        Optional<Product> optionalProduct =
                this.productRepository.findProductByDisplayedAndShopDisplayed(productId);

        if (optionalProduct.isEmpty()) {
            throw new GraphqlException("product not found");
        }

        return optionalProduct.get();
    }

    public List<Product> decreaseProductStockCount(List<ProductStockCountMessage> productStockCountMessages) {
        List<Product> productsToSave = new ArrayList<>();

        for (ProductStockCountMessage message : productStockCountMessages) {
            Product p = message.getProduct();

            if (p.getOptionsType() == OptionsType.Combination) {
                p.decreaseOptionStockCount(message.getStock(), message.getOptionId());
            }

            p.decreaseStockCount(message.getStock());
            productsToSave.add(p);
        }

        return productsToSave;
    }
}
