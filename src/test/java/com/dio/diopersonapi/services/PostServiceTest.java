package com.dio.diopersonapi.services;

import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.entities.Post;
import com.dio.diopersonapi.exception.PostNotFoundException;
import com.dio.diopersonapi.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dio.diopersonapi.utils.PostUtils.createFakeDTO;
import static com.dio.diopersonapi.utils.PostUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void testGivenPostDTOThenReturnSuccessSavedMessage() {
        PostDTO postDTO = createFakeDTO();
        Post expectedSavedPost = createFakeEntity();

        when(postRepository.save(any(Post.class))).thenReturn(expectedSavedPost);

        MessageResponseDTO expectedSuccessMessage = createExpectedSuccessMessage(expectedSavedPost);
        MessageResponseDTO successMessage = postService.createPost(postDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenPostDTOThenReturnSuccessUpdatedMessage() throws PostNotFoundException {
        PostDTO postDTO = createFakeDTO();
        Post expectedUpdatedPost = createFakeEntity();

        when(postRepository.save(any(Post.class))).thenReturn(expectedUpdatedPost);
        when(postRepository.findById(expectedUpdatedPost.getId())).thenReturn(Optional.of(expectedUpdatedPost));

        MessageResponseDTO expectedSuccessMessage = updateExpectedSuccessMessage(expectedUpdatedPost);
        MessageResponseDTO successMessage = postService.updateById(expectedUpdatedPost.getId(), postDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenPostDTOThenReturnPostNotFoundExceptionMessage() {
        PostDTO postDTO = createFakeDTO();
        Post expectedPost = createFakeEntity();

        when(postRepository.findById(expectedPost.getId())).thenReturn(Optional.empty());

        PostNotFoundException exception =
                assertThrows(
                        PostNotFoundException.class,
                        () -> postService.verifyIfExists(expectedPost.getId()));

        assertEquals(notFoundMessage(expectedPost).getMessage(), exception.getMessage());
    }

    @Test
    void testGivenPostDTOThenReturnListPostDTO(){
        Post expectedPost = createFakeEntity();

        when(postRepository.findAll()).thenReturn(Collections.singletonList(expectedPost));

        List<PostDTO> successList = postService.listAll();

        assertTrue(successList.size() > 0);
    }

    @Test
    void testGivenDeleteByIdThenNotThrow() throws PostNotFoundException {
        Post expectedPost = createFakeEntity();

        when(postRepository.findById(expectedPost.getId())).thenReturn(Optional.of(expectedPost));

        postService.delete(expectedPost.getId());

        verify(postRepository, times(1)).deleteById(expectedPost.getId());
    }

    @Test
    void testGivenFindByIdThenReturnPostDTO() throws PostNotFoundException {
        PostDTO expectedPostDTO = createFakeDTO();
        Post expectedPost = createFakeEntity();

        when(postRepository.findById(expectedPost.getId())).thenReturn(Optional.of(expectedPost));

        PostDTO successDTO = postService.findById(expectedPost.getId());

        assertNotNull(successDTO);
        assertEquals(expectedPostDTO.getTitle(), successDTO.getTitle());
        assertEquals(expectedPostDTO.getBody(), successDTO.getBody());
        assertEquals(expectedPostDTO.getAuthor().getFirstName(), successDTO.getAuthor().getFirstName());
        assertEquals(expectedPostDTO.getAuthor().getLastName(), successDTO.getAuthor().getLastName());
    }


    private MessageResponseDTO createExpectedSuccessMessage(Post expectedSavedPost) {
        return MessageResponseDTO
                .builder()
                .message("Created post with ID " + expectedSavedPost.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(Post expectedUpdatedPost) {
        return MessageResponseDTO
                .builder()
                .message("Updated post with ID " + expectedUpdatedPost.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(Post expectedUpdatedPost) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Post with ID %s not found!", expectedUpdatedPost.getId()))
                .build();
    }
}
