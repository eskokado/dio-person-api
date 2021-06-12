package com.dio.diopersonapi.mapper;

import com.dio.diopersonapi.dto.mapper.PostMapper;
import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.entities.Comment;
import com.dio.diopersonapi.entities.Post;
import com.dio.diopersonapi.utils.PostUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostMapperTest {
    @Autowired
    private PostMapper postMapper;

    @Test
    void testGivenPostDTOThenReturnPostEntity() {
        PostDTO postDTO = PostUtils.createFakeDTO();
        Post post = postMapper.toModel(postDTO);

        assertEquals(postDTO.getAuthor().getFirstName(), post.getAuthor().getFirstName());
        assertEquals(postDTO.getAuthor().getLastName(), post.getAuthor().getLastName());

        assertEquals(postDTO.getBody(), post.getBody());
        assertEquals(postDTO.getTitle(), post.getTitle());
        assertEquals(postDTO.getDate(), post.getDate().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" )));

        Comment comment = post.getComments().get(0);
        CommentDTO commentDTO = postDTO.getComments().get(0);

        assertEquals(commentDTO.getText(), comment.getText());
        assertEquals(commentDTO.getAuthor().getFirstName(), comment.getAuthor().getFirstName());
        assertEquals(commentDTO.getAuthor().getLastName(), comment.getAuthor().getLastName());
    }

    @Test
    void testGivenPostEntityThenReturnPostDTO() {
        Post post = PostUtils.createFakeEntity();
        PostDTO postDTO = postMapper.toDTO(post);

        assertEquals(post.getAuthor().getFirstName(), postDTO.getAuthor().getFirstName());
        assertEquals(post.getAuthor().getLastName(), postDTO.getAuthor().getLastName());


        assertEquals(post.getBody(), postDTO.getBody());
        assertEquals(post.getTitle(), postDTO.getTitle());
        assertEquals(post.getDate().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd")), postDTO.getDate());

        Comment comment = post.getComments().get(0);
        CommentDTO commentDTO = postDTO.getComments().get(0);

        assertEquals(comment.getText(), commentDTO.getText());
        assertEquals(comment.getAuthor().getFirstName(), commentDTO.getAuthor().getFirstName());
        assertEquals(comment.getAuthor().getLastName(), commentDTO.getAuthor().getLastName());
    }

}
