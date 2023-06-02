package org.example.dto.order;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Order;

@Getter
@Setter
public class OrderInfoResponse {
    private Order order;
}
