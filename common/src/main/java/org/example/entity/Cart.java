package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.ToString;

@TypeDef(name = "json", typeClass = JsonType.class)
@ToString
@Entity
@Getter
public class Cart extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private CartItemOption options;

    @Enumerated(EnumType.STRING)
    private OptionsType optionType;

    private Long quantity;

    public Cart(Product product, User user, CartItemOption options, OptionsType optionType, Long quantity) {
        this.product = product;
        this.user = user;
        this.options = options;
        this.optionType = optionType;
        this.quantity = quantity;
    }
}
