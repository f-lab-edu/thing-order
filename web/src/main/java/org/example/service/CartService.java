package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.dto.order.DeleteCartItemMessage;
import org.example.entity.Cart;
import org.example.entity.OptionsType;
import org.example.repository.CartRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<Cart> deleteCartItems(Long userId, List<DeleteCartItemMessage> deleteCartItemMessages) {
        List<Cart> cartsToDelete = new ArrayList<>();

        for (DeleteCartItemMessage message : deleteCartItemMessages) {
            if (message.getOptionsType() == OptionsType.Absence) {
                Optional<Cart> noOptionCartItem = this.cartRepository.findCartByUserIdAndAndProductIdAndOptionTypeIs(
                    userId, message.getProductId(), OptionsType.Absence);

                noOptionCartItem.ifPresent(cartsToDelete::add);
            } else {
                Optional<List<Cart>> optionCartItem = this.cartRepository.findCartsByUserIdAndAndProductIdAndOptionTypeIs(
                    userId, message.getProductId(), OptionsType.Combination);

                optionCartItem.ifPresent(carts -> {
                    carts.forEach(cart -> {
                        if (cart.getOptions().getOptionId() == message.getOptionId()) {
                            cartsToDelete.add(cart);
                        }
                    });
                });
            }
        }

        this.cartRepository.deleteAllInBatch(cartsToDelete);

        return cartsToDelete;
    }
}
