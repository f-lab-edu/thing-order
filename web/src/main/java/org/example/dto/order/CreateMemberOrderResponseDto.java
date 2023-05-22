package org.example.dto.order;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Order;

@Getter
@Setter
public class CreateMemberOrderResponseDto {
    private boolean ok;
    private Order results;
    private boolean isZeroPaidOrder;

    public CreateMemberOrderResponseDto(boolean ok, Order results, boolean isZeroPaidOrder) {
        this.ok = ok;
        this.results = results;
        this.isZeroPaidOrder = isZeroPaidOrder;
    }

    @Override
    public String toString() {
        return "CreateMemberOrderOutputDTO{" +
                "ok=" + ok +
                ", results=" + results +
                ", isZeroPaidOrder=" + isZeroPaidOrder +
                '}';
    }
}

