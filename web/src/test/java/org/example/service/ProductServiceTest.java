package org.example.service;

import org.example.exception.GraphqlException;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("존재하지 않는 상품을 주문할 시 예외를 던진다.")
    void checkProductExistTest() {
        // given
        Long notExistProductId = 1L;
        // when // then
        assertThatThrownBy(() -> productService.checkProductExist(List.of(notExistProductId)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("Could not find the product with ID");
    }
}
