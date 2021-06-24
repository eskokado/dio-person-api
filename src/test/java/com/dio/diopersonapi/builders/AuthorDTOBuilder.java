package com.dio.diopersonapi.builders;

import com.dio.diopersonapi.dto.request.AuthorDTO;
import lombok.Builder;

@Builder
public class AuthorDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String firstName = "AuthorFistName";

    @Builder.Default
    private String lastName = "AuthorLastName";

    public AuthorDTO toAuthorDTO() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(id);
        authorDTO.setFirstName(firstName);
        authorDTO.setLastName(lastName);
        return authorDTO;
    }
}
