package org.example.dto.order;

import org.example.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfoResponse {

    private Order order;
}
