package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.example.dto.order.DeleteCartItemMessage;
import org.example.entity.Cart;
import org.example.entity.CartItemOption;
import org.example.entity.DiscountType;
import org.example.entity.OptionsType;
import org.example.entity.Product;
import org.example.entity.ProductClassification;
import org.example.entity.ProductOption;
import org.example.entity.ShippingType;
import org.example.entity.Shop;
import org.example.entity.User;
import org.example.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @DisplayName("카트 아이템 삭제 테스트, 삭제할 카트 아이템의 목록을 받아서 유저별 장바구니에서 해당 카트 아이템들을 삭제한다.")
    @Test
    void deleteCartItems() {
        // given
        long optionProductId = 1000L;
        long optionIdOfOptionProduct = 1L;
        OptionsType optionsTypeOfOptionProduct = OptionsType.Combination;

        long noOptionProductId = 1001L;
        long tempUserId = 1L;
        OptionsType optionsTypeOfNoOptionProduct = OptionsType.Absence;

        Shop shop = new Shop("testShop", true);

        ProductOption productOption1 = new ProductOption(optionIdOfOptionProduct, "종류", "랜덤1마리", "", "", "", "", 144L,
            0L);
        ProductOption productOption2 = new ProductOption(2L, "색상", "파랑", "", "", "", "", 300L,
            0L);

        Product optionTestProduct = new Product("test product 1", DiscountType.Value, 2810L, 1690L, 4500L, false,
            ProductClassification.Partnership, ShippingType.Domestic, 3000L, 3000L, 3000L, true, 300L,
            optionsTypeOfOptionProduct, shop, List.of(productOption1, productOption2));
        optionTestProduct.setId(optionProductId);

        Product noOptionTestProduct = new Product("test product 2", DiscountType.Value, 1000L, 3500L, 4500L, false,
            ProductClassification.Partnership, ShippingType.Domestic, 5000L, 3000L, 3000L, true, 300L,
            optionsTypeOfNoOptionProduct, shop, null);
        noOptionTestProduct.setId(noOptionProductId);

        User user = new User(tempUserId, "temp@gmail.com", "temp user", "01012341234");

        CartItemOption cartItemOption = new CartItemOption(1L, null, "종류", "랜덤1마리", "", "", "", "", 0L);

        Cart cart1 = new Cart(optionTestProduct, user, cartItemOption, optionsTypeOfOptionProduct, 1L);
        Cart cart2 = new Cart(noOptionTestProduct, user, null, optionsTypeOfNoOptionProduct, 1L);

        given(cartRepository.findCartsByUserIdAndAndProductIdAndOptionTypeIs(tempUserId, optionProductId,
            optionsTypeOfOptionProduct)).willReturn(Optional.of(List.of(cart1)));
        given(cartRepository.findCartByUserIdAndAndProductIdAndOptionTypeIs(tempUserId, noOptionProductId,
            optionsTypeOfNoOptionProduct)).willReturn(
            Optional.of(cart2));

        // when
        DeleteCartItemMessage msg1 = new DeleteCartItemMessage(optionProductId, optionsTypeOfOptionProduct,
            optionIdOfOptionProduct);
        DeleteCartItemMessage msg2 = new DeleteCartItemMessage(noOptionProductId, optionsTypeOfNoOptionProduct, null);
        List<Cart> deletedCartItems = this.cartService.deleteCartItems(tempUserId, List.of(msg1, msg2));

        // then
        assertThat(deletedCartItems.size()).isEqualTo(2);
        assertThat(deletedCartItems.contains(cart1)).isTrue();
        assertThat(deletedCartItems.contains(cart2)).isTrue();
    }
}
