package com.dio.diopersonapi.builders;

import com.dio.diopersonapi.dto.request.PersonDTO;
import com.dio.diopersonapi.dto.request.PhoneDTO;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public class PersonDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String firstName = "PersonFistName";

    @Builder.Default
    private String lastName = "PersonLastName";

    @Builder.Default
    private String cpf = "458.559.870-78";

    @Builder.Default
    private String birthDate = "2021-06-24";

    @Builder.Default
    private List<PhoneDTO> phones = Collections.singletonList(PhoneDTOBuilder.builder().build().toPhoneDTO());

    public PersonDTO toPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(id);
        personDTO.setFirstName(firstName);
        personDTO.setLastName(lastName);
        personDTO.setCpf(cpf);
        personDTO.setBirthDate(birthDate);
        personDTO.setPhones(phones);
        return personDTO;
    }
}
