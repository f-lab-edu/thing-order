package org.example.repository;

import org.example.config.TestConfig;
import org.example.entity.Product;
import org.example.entity.Shop;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = {TestConfig.class, ProductRepository.class}, properties = "spring.config" +
        ".name=application-common-test")
@EnableAutoConfiguration
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @DisplayName("상품의 isDisplayed가 true이고 상품이 속한 상점의 isDisplayed가 true일 경우 해당 상품을 리턴한다.")
    @Test
    void findProductByDisplayedAndShopDisplayed() {
        // given
        Shop shopToSave = new Shop("test shop", true);
        Shop displayedTrueShop = this.shopRepository.save(shopToSave);
        Product productToSave = new Product(true, displayedTrueShop);
        Product displayedTrueProduct = this.productRepository.save(productToSave);

        // when
        Optional<Product> productOptional =
                this.productRepository.findProductByDisplayedAndShopDisplayed(displayedTrueProduct.getId());

        // then
        assertThat(productOptional.isPresent()).isEqualTo(true);
        assertThat(productOptional.get().getId()).isEqualTo(displayedTrueProduct.getId());
    }

    @DisplayName("상품의 isDisplayed가 false이면 select 되지 않는다.")
    @Test
    void findProductByDisplayedAndShopDisplayed2() {
        // given
        Shop shopToSave = new Shop("test shop", true);
        Shop displayedTrueShop = this.shopRepository.save(shopToSave);
        Product productToSave = new Product(false, displayedTrueShop);
        Product displayedFalseProduct = this.productRepository.save(productToSave);

        // when
        Optional<Product> productOptional =
                this.productRepository.findProductByDisplayedAndShopDisplayed(displayedFalseProduct.getId());

        // then
        assertThat(productOptional.isEmpty()).isEqualTo(true);
    }

    @DisplayName("상점의 isDisplayed가 false이면 select 되지 않는다.")
    @Test
    void findProductByDisplayedAndShopDisplayed3() {
        // given
        Shop shopToSave = new Shop("test shop", false);
        Shop dispaleydFalseShop = this.shopRepository.save(shopToSave);
        Product productToSave = new Product(true, dispaleydFalseShop);
        Product displayedTrueProduct = this.productRepository.save(productToSave);

        // when
        Optional<Product> productOptional =
                this.productRepository.findProductByDisplayedAndShopDisplayed(displayedTrueProduct.getId());

        // then
        assertThat(productOptional.isEmpty()).isEqualTo(true);
    }
}
