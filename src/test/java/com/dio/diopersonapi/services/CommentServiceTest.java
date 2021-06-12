package com.dio.diopersonapi.services;

import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.entities.Comment;
import com.dio.diopersonapi.exception.CommentNotFoundException;
import com.dio.diopersonapi.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dio.diopersonapi.utils.CommentUtils.createFakeDTO;
import static com.dio.diopersonapi.utils.CommentUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testGivenCommentDTOThenReturnSuccessSavedMessage() {
        CommentDTO commentDTO = createFakeDTO();
        Comment expectedSavedComment = createFakeEntity();

        when(commentRepository.save(any(Comment.class))).thenReturn(expectedSavedComment);

        MessageResponseDTO expectedSuccessMessage = createExpectedSuccessMessage(expectedSavedComment);
        MessageResponseDTO successMessage = commentService.createComment(commentDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenCommentDTOThenReturnSuccessUpdatedMessage() throws CommentNotFoundException {
        CommentDTO commentDTO = createFakeDTO();
        Comment expectedUpdatedComment = createFakeEntity();

        when(commentRepository.save(any(Comment.class))).thenReturn(expectedUpdatedComment);
        when(commentRepository.findById(expectedUpdatedComment.getId())).thenReturn(Optional.of(expectedUpdatedComment));

        MessageResponseDTO expectedSuccessMessage = updateExpectedSuccessMessage(expectedUpdatedComment);
        MessageResponseDTO successMessage = commentService.updateById(expectedUpdatedComment.getId(), commentDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenCommentDTOThenReturnCommentNotFoundExceptionMessage() {
        CommentDTO commentDTO = createFakeDTO();
        Comment expectedComment = createFakeEntity();

        when(commentRepository.findById(expectedComment.getId())).thenReturn(Optional.empty());

        CommentNotFoundException exception =
                assertThrows(
                        CommentNotFoundException.class,
                        () -> commentService.verifyIfExists(expectedComment.getId()));

        assertEquals(notFoundMessage(expectedComment).getMessage(), exception.getMessage());
    }

    @Test
    void testGivenCommentDTOThenReturnListCommentDTO(){
        Comment expectedComment = createFakeEntity();

        when(commentRepository.findAll()).thenReturn(Collections.singletonList(expectedComment));

        List<CommentDTO> successList = commentService.listAll();

        assertTrue(successList.size() > 0);
    }

    @Test
    void testGivenDeleteByIdThenNotThrow() throws CommentNotFoundException {
        Comment expectedComment = createFakeEntity();

        when(commentRepository.findById(expectedComment.getId())).thenReturn(Optional.of(expectedComment));

        commentService.delete(expectedComment.getId());

        verify(commentRepository, times(1)).deleteById(expectedComment.getId());
    }

    @Test
    void testGivenFindByIdThenReturnCommentDTO() throws CommentNotFoundException {
        CommentDTO expectedCommentDTO = createFakeDTO();
        Comment expectedComment = createFakeEntity();

        when(commentRepository.findById(expectedComment.getId())).thenReturn(Optional.of(expectedComment));

        CommentDTO successDTO = commentService.findById(expectedComment.getId());

        assertNotNull(successDTO);
        assertEquals(expectedCommentDTO.getText(), successDTO.getText());
        assertEquals(expectedCommentDTO.getAuthor().getFirstName(), successDTO.getAuthor().getFirstName());
        assertEquals(expectedCommentDTO.getAuthor().getLastName(), successDTO.getAuthor().getLastName());
    }


    private MessageResponseDTO createExpectedSuccessMessage(Comment expectedSavedComment) {
        return MessageResponseDTO
                .builder()
                .message("Created comment with ID " + expectedSavedComment.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(Comment expectedUpdatedComment) {
        return MessageResponseDTO
                .builder()
                .message("Updated comment with ID " + expectedUpdatedComment.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(Comment expectedUpdatedComment) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Comment with ID %s not found!", expectedUpdatedComment.getId()))
                .build();
    }
}
