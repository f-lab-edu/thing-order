package org.example.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.example.dto.order.CheckAdditionalDeliveryFeeOutput;
import org.example.entity.AdditionalDeliveryFeeArea;
import org.example.entity.AreaType;
import org.example.repository.AdditionalDeliveryFeeAreaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdditionalDeliveryServiceTest {

    @Mock
    private AdditionalDeliveryFeeAreaRepository additionalDeliveryFeeAreaRepository;
    @InjectMocks
    private AdditionalDeliveryService additionalDeliveryService;

    @DisplayName("zipCode가 추가 배송지역에 포함되면 CheckAdditionalDeliveryFeeOutput의 " +
        "isAddressToChargeAdditionalFee의 값이 true이고 areaType의 적절한 값이 있어야 한다.")
    @ParameterizedTest()
    @CsvSource({"Jeju", "AreaExceptForJeju"})
    void checkAdditionalDeliveryFeeTest1(AreaType areaType) {
        // given
        AdditionalDeliveryFeeArea jejuDeliveryAddress = new AdditionalDeliveryFeeArea("123456",
            "temp street address", areaType);

        given(additionalDeliveryFeeAreaRepository.findAdditionalDeliveryFeeAreaByZipCode("123456"))
            .willReturn(Optional.of(jejuDeliveryAddress));

        // when
        CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput = additionalDeliveryService.checkAdditionalDeliveryFee(
            "123456");

        // then
        assertThat(checkAdditionalDeliveryFeeOutput.isAddressToChargeAdditionalFee()).isEqualTo(
            true);
        assertThat(checkAdditionalDeliveryFeeOutput.getAreaType()).isEqualTo(areaType);
    }

    @DisplayName("zipCode가 추가 배송지역에 포함되지 않으면 CheckAdditionalDeliveryFeeOutput의 " +
        "isAddressToChargeAdditionalFee의 값이 false이다")
    @Test
    void checkAdditionalDeliveryFeeTest3() {
        // given

        // when
        CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput = additionalDeliveryService.checkAdditionalDeliveryFee(
            "123456");

        // then
        assertThat(checkAdditionalDeliveryFeeOutput.isAddressToChargeAdditionalFee()).isEqualTo(
            false);
        assertThat(checkAdditionalDeliveryFeeOutput.getAreaType()).isNull();
    }
}
