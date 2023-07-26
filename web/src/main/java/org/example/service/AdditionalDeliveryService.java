package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.AdditionalDeliveryFeeArea;
import org.example.message.CheckAdditionalDeliveryFeeOutput;
import org.example.repository.AdditionalDeliveryFeeAreaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
