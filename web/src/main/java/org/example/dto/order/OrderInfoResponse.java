package org.example.dto.order;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.Order;

@Getter
@Setter
public class OrderInfoResponse {
    private Order order;
}
