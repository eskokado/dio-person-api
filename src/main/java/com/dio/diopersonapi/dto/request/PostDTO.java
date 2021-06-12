package com.dio.diopersonapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;

    @NotEmpty
    @Size(min=2)
    private String title;

    @NotEmpty
    @Size(min=2)
    private String body;

    @NotEmpty
    private String date;

    private AuthorDTO author;
}
