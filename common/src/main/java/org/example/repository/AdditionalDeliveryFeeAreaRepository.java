package org.example.repository;

import java.util.Optional;

import org.example.entity.AdditionalDeliveryFeeArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalDeliveryFeeAreaRepository extends
    JpaRepository<AdditionalDeliveryFeeArea, Long> {

    Optional<AdditionalDeliveryFeeArea> findAdditionalDeliveryFeeAreaByZipCode(String zipCode);
}
