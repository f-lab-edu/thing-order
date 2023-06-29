package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.entity.Cart;
import org.example.entity.OptionsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserIdAndAndProductIdAndOptionTypeIs(long userId, long productId,
        OptionsType optionsType);

    Optional<List<Cart>> findCartsByUserIdAndAndProductIdAndOptionTypeIs(long userId, long productId,
        OptionsType optionsType);
}
