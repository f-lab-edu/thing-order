package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;

import org.example.config.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String coverImg;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "product_discount_type_enum")
    @Type(type = "psql_enum")
    private DiscountType discountType;

    private Long discountRate;

    private Long discountPrice;

    private Long price;

    private boolean isFreeShipping;

    @Enumerated(EnumType.STRING)
    private ProductClassification classification;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "product_shipping_type_enum")
    @Type(type = "psql_enum")
    private ShippingType shippingType;

    private Long baseShippingFee;

    private Long jejuShippingFee;

    private Long islandShippingFee;

    private boolean isDisplayed;

    private LocalDateTime estimatedWarehousingDate;

    private LocalDateTime preReserveEndTime;

    @Enumerated(EnumType.STRING)
    private EstimatedWarehousingDateType estimatedWarehousingDateType;

    private boolean isCrawled;

    private String crawlLink;

    private Long stockCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "product_options_type_enum")
    @Type(type = "psql_enum")
    private OptionsType optionsType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<ProductOption> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "product")
    private final List<OrderItem> orderItem = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private final List<ProductCoupon> productCoupon = new ArrayList<>();

    public Product(boolean isDisplayed, Shop shop) {
        this.isDisplayed = isDisplayed;
        this.shop = shop;
    }

    public Product(long id, String name, Long stockCount) {
        this.id = id;
        this.name = name;
        this.stockCount = stockCount;
    }

    public Product(long id, String name, Long stockCount, OptionsType optionsType,
        List<ProductOption> options) {
        this.id = id;
        this.name = name;
        this.stockCount = stockCount;
        this.optionsType = optionsType;
        this.options = options;
    }

    public Product(String name, DiscountType discountType, Long discountRate, Long discountPrice, Long price,
        boolean isFreeShipping, ProductClassification classification, ShippingType shippingType, Long baseShippingFee,
        Long jejuShippingFee, Long islandShippingFee, boolean isDisplayed, Long stockCount, OptionsType optionsType,
        Shop shop, List<ProductOption> productOptions) {
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.price = price;
        this.isFreeShipping = isFreeShipping;
        this.classification = classification;
        this.shippingType = shippingType;
        this.baseShippingFee = baseShippingFee;
        this.jejuShippingFee = jejuShippingFee;
        this.islandShippingFee = islandShippingFee;
        this.isDisplayed = isDisplayed;
        this.stockCount = stockCount;
        this.optionsType = optionsType;
        this.shop = shop;
        this.options = productOptions;
    }
}
