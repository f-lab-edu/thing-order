package org.example.dto.order;

import org.example.domain.*;

public class CreateMemberOrderOutputDTO {
    private boolean ok;
    private Order results;
    private boolean isZeroPaidOrder;

    public CreateMemberOrderOutputDTO(boolean ok, Order results, boolean isZeroPaidOrder) {
        this.ok = ok;
        this.results = results;
        this.isZeroPaidOrder = isZeroPaidOrder;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Order getResults() {
        return results;
    }

    public void setResults(Order results) {
        this.results = results;
    }

    public boolean isZeroPaidOrder() {
        return isZeroPaidOrder;
    }

    public void setZeroPaidOrder(boolean zeroPaidOrder) {
        isZeroPaidOrder = zeroPaidOrder;
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

