package org.example.dto.order;

public class GetOrderByUserOutputDTO {
    private boolean ok;
    private OrderInfo results;

    public GetOrderByUserOutputDTO(boolean ok, OrderInfo results) {
        this.ok = ok;
        this.results = results;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public OrderInfo getResults() {
        return results;
    }

    public void setResults(OrderInfo results) {
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
