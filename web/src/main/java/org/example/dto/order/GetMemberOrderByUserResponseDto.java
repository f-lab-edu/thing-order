package org.example.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class GetMemberOrderByUserResponseDto {
    private boolean ok;
    private OrderInfoResponse results;

    public GetMemberOrderByUserResponseDto(boolean ok, OrderInfoResponse results) {
        this.ok = ok;
        this.results = results;
    }
}
