package org.example.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderByUserOutputDto {
    private boolean ok;
    private OrderInfoResponse results;

    public GetOrderByUserOutputDto(boolean ok, OrderInfoResponse results) {
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
