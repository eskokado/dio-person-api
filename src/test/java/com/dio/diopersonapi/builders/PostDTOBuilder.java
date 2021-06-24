package com.dio.diopersonapi.builders;

import com.dio.diopersonapi.dto.request.AuthorDTO;
import com.dio.diopersonapi.dto.request.PostDTO;
import lombok.Builder;

@Builder
public class PostDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String title = "Post Title";

    @Builder.Default
    private String body = "Body Builder String Faker";

    @Builder.Default
    private String date = "2021-06-24";

    @Builder.Default
    private AuthorDTO author = AuthorDTOBuilder.builder().build().toAuthorDTO();

    public PostDTO toPostDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(id);
        postDTO.setTitle(title);
        postDTO.setBody(body);
        postDTO.setDate(date);
        postDTO.setAuthor(author);
        return postDTO;
    }
}
