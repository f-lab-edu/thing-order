package org.example.service;

import org.example.dto.CreateOrderItemRequest;
import org.example.entity.OptionsType;
import org.example.entity.Product;
import org.example.entity.ProductOption;
import org.example.entity.StatusOfStock;
import org.example.exception.GraphqlException;
import org.example.message.ProductStockCountMessage;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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

    @Test
    @DisplayName("옵션 없는 상품 재고가 부족할 경우 예외를 던진다.")
    void checkProductStockTest() {
        // given
        long productIdToOrder = 1L;
        Long quantityToOrder = 2L;
        String tempProductName = "테스트 상품";

        CreateOrderItemRequest mockedCreateOrderItemRequest = new CreateOrderItemRequest();
        mockedCreateOrderItemRequest.setProductId(productIdToOrder);
        mockedCreateOrderItemRequest.setOrderQuantity(quantityToOrder);

        Product product = new Product(1L, tempProductName, 0L);

        given(productRepository.findById(productIdToOrder)).willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(
                () -> productService.checkProductStockCount(List.of(mockedCreateOrderItemRequest)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("stock count less than order quantity")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "LACK_OF_STOCK_COUNT");

                        List<String> soldOutProductNames = (List<String>) graphqlException.getExtensions()
                                .get("soldOutProductName");
                        assertThat(soldOutProductNames).isNotNull();
                        assertThat(soldOutProductNames.size()).isEqualTo(1);
                        assertThat(soldOutProductNames.get(0)).isEqualTo(tempProductName);
                    }
                });
    }

    @Test
    @DisplayName("옵션 있는 상품(optionType = combination) 재고가 부족할 경우 예외를 던진다.")
    void checkProductStockTest2() {
        // given
        long productIdToOrder = 1L;
        Long quantityToOrder = 4L;
        Long optionIdToOrder = 1L;
        String tempProductName = "테스트 상품";

        CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest();
        createOrderItemRequest.setProductId(productIdToOrder);
        createOrderItemRequest.setOptionId(optionIdToOrder);
        createOrderItemRequest.setOrderQuantity(quantityToOrder);

        ProductOption productOption = new ProductOption(1L, "색상", "빨강", 3L, StatusOfStock.OnSale);

        Product product = new Product(1L, tempProductName, 4L, OptionsType.Combination,
                List.of(productOption));

        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(
                () -> productService.checkProductStockCount(List.of(createOrderItemRequest)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("stock count less than order quantity")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "LACK_OF_STOCK_COUNT");

                        List<String> soldOutProductNames = (List<String>) graphqlException.getExtensions()
                                .get("soldOutProductName");
                        assertThat(soldOutProductNames).isNotNull();
                        assertThat(soldOutProductNames.size()).isEqualTo(1);
                        assertThat(soldOutProductNames.get(0)).isEqualTo(tempProductName + " / 옵션" +
                                " 재고 부족");
                    }
                });
    }

    @Test
    @DisplayName("옵션 있는 상품(optionType = combination)과 옵션 없는 상품을 동시에 주문 시 재고가 부족한 경우 예외를 던진다.")
    void checkProductStockTest3() {
        // given
        long lackStockCountOptionProductId = 1L;

        CreateOrderItemRequest createOrderItemRequest1 = new CreateOrderItemRequest();
        createOrderItemRequest1.setProductId(lackStockCountOptionProductId);
        createOrderItemRequest1.setOptionId(1L);
        createOrderItemRequest1.setOrderQuantity(4L);

        long enoughStockCountProductId = 2L;

        CreateOrderItemRequest createOrderItemRequest2 = new CreateOrderItemRequest();
        createOrderItemRequest2.setProductId(enoughStockCountProductId);
        createOrderItemRequest2.setOptionId(1L);
        createOrderItemRequest2.setOrderQuantity(4L);

        ProductOption productOption = new ProductOption(1L, "색상", "빨강", 3L, StatusOfStock.OnSale);

        String tempOptionProductName = "테스트 옵션 상품";

        Product product = new Product(1L, tempOptionProductName, 4L, OptionsType.Combination,
                List.of(productOption));

        String tempProductName = "테스트 상품";

        Product product2 = new Product(2L, tempProductName, 10L);

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRepository.findById(2L)).willReturn(Optional.of(product2));

        // when
        // then
        assertThatThrownBy(() -> productService.checkProductStockCount(
                List.of(createOrderItemRequest1, createOrderItemRequest2)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("stock count less than order quantity")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "LACK_OF_STOCK_COUNT");

                        List<String> soldOutProductNames = (List<String>) graphqlException.getExtensions()
                                .get("soldOutProductName");
                        assertThat(soldOutProductNames).isNotNull();
                        assertThat(soldOutProductNames.size()).isEqualTo(1);
                        assertThat(soldOutProductNames.get(0)).isEqualTo(
                                tempOptionProductName + " / 옵션" +
                                        " 재고 부족");
                    }
                });
    }

    @DisplayName("옵션 없는 상품 재고 감소 테스트")
    @Test
    void decreaseProductStockCount1() {
        // given
        Product product = new Product(1L, "product1", 200L, OptionsType.Absence, null);
        ProductStockCountMessage message = new ProductStockCountMessage(product, null, 1L);

        // when
        List<Product> products = productService.decreaseProductStockCount(List.of(message));

        // then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.contains(product)).isTrue();
        assertThat(products.get(0).getStockCount()).isEqualTo(199L);
    }

    @DisplayName("옵션 상품 재고 감소 테스트")
    @Test
    void decreaseProductStockCount2() {
        // given
        ProductOption option1 = new ProductOption(1L, "색상", "빨강", 100L, StatusOfStock.OnSale);
        ProductOption option2 = new ProductOption(2L, "색상", "파랑", 100L, StatusOfStock.OnSale);
        Product product = new Product(1L, "product1", 200L, OptionsType.Combination, List.of(option1, option2));
        ProductStockCountMessage message = new ProductStockCountMessage(product, 1L, 1L);

        // when
        List<Product> products = productService.decreaseProductStockCount(List.of(message));

        // then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.contains(product)).isTrue();
        assertThat(products.get(0).getStockCount()).isEqualTo(199L);
        assertThat(products.get(0)
                .getOptions()
                .stream()
                .filter(option -> option.getOptionId() == 1L)
                .findFirst()
                .get()
                .getStockCount()).isEqualTo(99L);
    }
}
