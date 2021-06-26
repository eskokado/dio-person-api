package com.dio.diopersonapi.builders;

import com.dio.diopersonapi.dto.request.AuthorDTO;
import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.request.PostDTO;
import lombok.Builder;

@Builder
public class CommentDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String text = "Text Comment";

    @Builder.Default
    private String date = "2021-06-24";

    @Builder.Default
    private AuthorDTO author = AuthorDTOBuilder.builder().build().toAuthorDTO();

    @Builder.Default
    private PostDTO post = PostDTOBuilder.builder().build().toPostDTO();

    public CommentDTO toCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setText(text);
        commentDTO.setDate(date);
        commentDTO.setAuthor(author);
        commentDTO.setPost(post);
        return commentDTO;
    }
}
