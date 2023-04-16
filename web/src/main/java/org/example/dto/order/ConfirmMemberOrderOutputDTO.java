package org.example.dto.order;

import lombok.*;

@Getter
@Setter
public class ConfirmMemberOrderOutputDTO {
    private boolean ok;
    private boolean results;

    public ConfirmMemberOrderOutputDTO(boolean ok, boolean results) {
        this.ok = ok;
        this.results = results;
    }
}
