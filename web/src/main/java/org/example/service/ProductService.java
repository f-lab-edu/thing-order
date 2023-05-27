package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    void checkProductExist(List<Long> productIdsToOrder) {
        for (Long productId : productIdsToOrder) {
            Optional<Product> product = productRepository.findById(productId);
            product.orElseThrow(() -> new GraphqlException("Could not find the product with ID"));
        }
    }
}
