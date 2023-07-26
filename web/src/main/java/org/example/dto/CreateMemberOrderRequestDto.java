package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.PaymentMethod;

import java.util.List;

@Getter
@Setter
public class CreateMemberOrderRequestDto {

    private Long deliveryId;
    private String deliveryMessage;
    private List<CreateOrderItemRequest> items;
    private Long pointDiscountPrice;
    private PaymentMethod paymentMethod;
}

