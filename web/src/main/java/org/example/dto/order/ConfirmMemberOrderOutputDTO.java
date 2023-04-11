package org.example.dto.order;

public class ConfirmMemberOrderOutputDTO {
    private boolean ok;
    private boolean results;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isResults() {
        return results;
    }

    public void setResults(boolean results) {
        this.results = results;
    }

    public ConfirmMemberOrderOutputDTO(boolean ok, boolean results) {
        this.ok = ok;
        this.results = results;
    }
}
