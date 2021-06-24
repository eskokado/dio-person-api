package com.dio.diopersonapi.builders;

import com.dio.diopersonapi.dto.request.PhoneDTO;
import com.dio.diopersonapi.enums.PhoneType;
import lombok.Builder;

@Builder
public class PhoneDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private PhoneType type = PhoneType.HOME;

    @Builder.Default
    private String number = "1122334455667";

    public PhoneDTO toPhoneDTO() {
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setId(id);
        phoneDTO.setType(type);
        phoneDTO.setNumber(number);
        return phoneDTO;
    }
}
