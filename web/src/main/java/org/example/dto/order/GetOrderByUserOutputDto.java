package org.example.dto.order;

public class GetOrderByUserOutputDto {
    private boolean ok;
    private OrderInfoResponse results;

    public GetOrderByUserOutputDto(boolean ok, OrderInfoResponse results) {
        this.ok = ok;
        this.results = results;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public OrderInfoResponse getResults() {
        return results;
    }

    public void setResults(OrderInfoResponse results) {
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
