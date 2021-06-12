package com.dio.diopersonapi.mapper;

import com.dio.diopersonapi.dto.mapper.CommentMapper;
import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.entities.Comment;
import com.dio.diopersonapi.utils.CommentUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void testGivenCommentDTOThenReturnCommentEntity() {
        CommentDTO commentDTO = CommentUtils.createFakeDTO();
        Comment comment = commentMapper.toModel(commentDTO);

        assertEquals(commentDTO.getAuthor().getFirstName(), comment.getAuthor().getFirstName());
        assertEquals(commentDTO.getAuthor().getLastName(), comment.getAuthor().getLastName());
        assertEquals(commentDTO.getText(), comment.getText());
        assertEquals(commentDTO.getDate(), comment.getDate().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd")));
    }

    @Test
    void testGivenCommentEntityThenReturnCommentDTO() {
        Comment comment = CommentUtils.createFakeEntity();
        CommentDTO commentDTO = commentMapper.toDTO(comment);

        assertEquals(comment.getAuthor().getFirstName(), commentDTO.getAuthor().getFirstName());
        assertEquals(comment.getAuthor().getLastName(), commentDTO.getAuthor().getLastName());
        assertEquals(comment.getText(), commentDTO.getText());
        assertEquals(comment.getDate().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd")), commentDTO.getDate());
    }

}
