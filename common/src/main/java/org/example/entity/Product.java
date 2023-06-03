package org.example.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@ToString
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String coverImg;

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Long discountRate;

    private Long discountPrice;

    private Long price;

    private boolean isFreeShipping;

    @Enumerated(EnumType.STRING)
    private ProductClassification classification;

    @Enumerated(EnumType.STRING)
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
    private OptionsType optionsType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<ProductOption> options;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItem = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductCoupon> productCoupon = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Product(boolean isDisplayed, Shop shop) {
        this.isDisplayed = isDisplayed;
        this.shop = shop;
    }
}
