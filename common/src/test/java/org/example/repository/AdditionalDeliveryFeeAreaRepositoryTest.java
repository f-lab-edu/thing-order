package org.example.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Optional;

import org.example.config.TestConfig;
import org.example.entity.AdditionalDeliveryFeeArea;
import org.example.entity.AreaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestConfig.class, AdditionalDeliveryFeeAreaRepository.class},
    properties = "spring.config.name=application-common-test")
@EnableAutoConfiguration
class AdditionalDeliveryFeeAreaRepositoryTest {

    @Autowired
    private AdditionalDeliveryFeeAreaRepository additionalDeliveryFeeAreaRepository;

    @DisplayName("우편 번호로 추가 배송비 지역 조회하기")
    @Test
    void findAdditionalDeliveryFeeAreaByZipCodeTest() {
        // given
        AdditionalDeliveryFeeArea additionalDeliveryFeeAreaToSave = new AdditionalDeliveryFeeArea(
            "123456", "temp street address", AreaType.Jeju);

        AdditionalDeliveryFeeArea saved =
            additionalDeliveryFeeAreaRepository.save(additionalDeliveryFeeAreaToSave);

        // when
        Optional<AdditionalDeliveryFeeArea> optionalAdditionalDeliveryFeeArea =
            additionalDeliveryFeeAreaRepository.findAdditionalDeliveryFeeAreaByZipCode(
                "123456");

        // then
        assertThat(optionalAdditionalDeliveryFeeArea.isEmpty()).isFalse();
        optionalAdditionalDeliveryFeeArea.ifPresent(additionalDeliveryFeeArea -> assertThat(
            additionalDeliveryFeeArea.getId()).isEqualTo(saved.getId()));
    }
}
