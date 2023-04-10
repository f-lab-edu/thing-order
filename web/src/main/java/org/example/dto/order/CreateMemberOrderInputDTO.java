package org.example.dto.order;

import java.util.List;

public class CreateMemberOrderInputDTO {
    private Long deliveryId;
    private String deliveryMessage;
    private List<CreateOrderItemInput> items;
    private Long pointDiscountPrice;
}

