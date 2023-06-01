package org.example.repository;

import org.example.entity.AdditionalDeliveryFeeArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionalDeliveryFeeAreaRepository  extends JpaRepository<AdditionalDeliveryFeeArea, Long> {
    Optional<AdditionalDeliveryFeeArea> findAdditionalDeliveryFeeAreaByZipCode(String zipCode);
}
