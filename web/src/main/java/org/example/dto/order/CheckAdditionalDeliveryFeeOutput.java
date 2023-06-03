package org.example.dto.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.entity.AreaType;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckAdditionalDeliveryFeeOutput {

    private final boolean ok;
    private final boolean isAddressToChargeAdditionalFee;
    private final AreaType areaType;

    public static CheckAdditionalDeliveryFeeOutput of(boolean ok,
            boolean isAddressToChargeAdditionalFee,
            AreaType areaType) {
        return new CheckAdditionalDeliveryFeeOutput(ok, isAddressToChargeAdditionalFee, areaType);
    }
}
