package org.example.dto.order;

import java.util.List;

public class CreateMemberOrderRequestDto {

    private Long deliveryId;
    private String deliveryMessage;
    private List<CreateOrderItemRequest> items;
    private Long pointDiscountPrice;
}

