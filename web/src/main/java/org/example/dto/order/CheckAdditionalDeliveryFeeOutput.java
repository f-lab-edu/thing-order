package org.example.dto.order;

import org.example.entity.AreaType;

import lombok.Getter;

@Getter
public class CheckAdditionalDeliveryFeeOutput {

    private final boolean ok;
    private final boolean isAddressToChargeAdditionalFee;
    private final AreaType areaType;

    public CheckAdditionalDeliveryFeeOutput(boolean ok, boolean isAddressToChargeAdditionalFee,
                                            AreaType areaType) {
        this.ok = ok;
        this.isAddressToChargeAdditionalFee = isAddressToChargeAdditionalFee;
        this.areaType = areaType;
    }
}
