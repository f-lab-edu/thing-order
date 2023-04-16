package org.example.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderByUserOutputDTO {
    private boolean ok;
    private OrderInfo results;

    public GetOrderByUserOutputDTO(boolean ok, OrderInfo results) {
        this.ok = ok;
        this.results = results;
    }

    @Override
    public String toString() {
        return "GetOrderByUserOutputDTO{" +
                "ok=" + ok +
                ", results=" + results +
                '}';
    }
}
