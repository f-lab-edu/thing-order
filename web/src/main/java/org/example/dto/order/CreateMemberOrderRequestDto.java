package org.example.dto.order;

import java.util.List;

import org.example.entity.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemberOrderRequestDto {

    private Long deliveryId;
    private String deliveryMessage;
    private List<CreateOrderItemRequest> items;
    private Long pointDiscountPrice;
    private PaymentMethod paymentMethod;
}

