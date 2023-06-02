package org.example.repository;

import org.example.config.TestConfig;
import org.example.entity.AdditionalDeliveryFeeArea;
import org.example.entity.AreaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
        AdditionalDeliveryFeeArea additionalDeliveryFeeAreaToSave =
                AdditionalDeliveryFeeArea.builder()
                        .areaType(AreaType.Jeju)
                        .zipCode("123456")
                        .streetAddress("temp street address")
                        .build();

        AdditionalDeliveryFeeArea saved =
                additionalDeliveryFeeAreaRepository.save(additionalDeliveryFeeAreaToSave);

        // when
        Optional<AdditionalDeliveryFeeArea> optionalAdditionalDeliveryFeeArea =
                additionalDeliveryFeeAreaRepository.findAdditionalDeliveryFeeAreaByZipCode("123456");

        // then
        assertThat(optionalAdditionalDeliveryFeeArea.isEmpty()).isFalse();
        assertThat(optionalAdditionalDeliveryFeeArea.get().getId()).isEqualTo(saved.getId());
    }
}
