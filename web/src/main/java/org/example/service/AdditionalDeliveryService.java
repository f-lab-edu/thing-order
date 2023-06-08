package org.example.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.CheckAdditionalDeliveryFeeOutput;
import org.example.entity.AdditionalDeliveryFeeArea;
import org.example.repository.AdditionalDeliveryFeeAreaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdditionalDeliveryService {

    private final AdditionalDeliveryFeeAreaRepository additionalDeliveryFeeAreaRepository;

    CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFee(String zipCode) {
        Optional<AdditionalDeliveryFeeArea> deliveryFeeArea =
                this.additionalDeliveryFeeAreaRepository.findAdditionalDeliveryFeeAreaByZipCode(
                        zipCode);

        return deliveryFeeArea.map(
                        additionalDeliveryFeeArea -> new CheckAdditionalDeliveryFeeOutput(true, true,
                                additionalDeliveryFeeArea.getAreaType()))
                .orElseGet(() -> new CheckAdditionalDeliveryFeeOutput(true, false, null));
    }
}
