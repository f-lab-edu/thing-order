package org.example.repository;

import java.util.Optional;
import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p inner join Shop s on p.shop.id = s.id where s.isDisplayed " +
            "= " +
            "true " +
            "and p" +
            ".isDisplayed = true and p.id = :productId")
    Optional<Product> findProductByDisplayedAndShopDisplayed(@Param("productId") long id);
}
