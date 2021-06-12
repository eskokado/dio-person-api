package com.dio.diopersonapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min=2)
    private String title;

    @NotEmpty
    @Size(min=2)
    private String body;

    @NotEmpty
    private String date;

    @NotEmpty
    private AuthorDTO author;

    @NotEmpty
    private List<CommentDTO> comments;
}
