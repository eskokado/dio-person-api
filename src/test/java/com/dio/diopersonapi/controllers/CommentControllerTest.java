package com.dio.diopersonapi.controllers;

import com.dio.diopersonapi.builders.CommentDTOBuilder;
import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.exception.CommentNotFoundException;
import com.dio.diopersonapi.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.dio.diopersonapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    private static final String COMMENT_API_URL_PATH = "/api/v1/comments";
    private static final long VALID_COMMENT_ID = 1L;
    private static final long INVALID_COMMENT_ID = 2l;

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenACommentIsCreated() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        MessageResponseDTO messageResponseDTO = createExpectedSuccessMessage(commentDTO);
        when(commentService.createComment(commentDTO)).thenReturn(messageResponseDTO);

        // then
        mockMvc.perform(post(COMMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(messageResponseDTO.getMessage())));
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        doThrow(CommentNotFoundException.class).when(commentService).updateById(INVALID_COMMENT_ID, commentDTO);

        // then
        mockMvc.perform(put(COMMENT_API_URL_PATH + "/" + INVALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        MessageResponseDTO messageResponseDTO = updateExpectedSuccessMessage(commentDTO);
        when(commentService.updateById(VALID_COMMENT_ID, commentDTO)).thenReturn(messageResponseDTO);

        // then
        mockMvc.perform(put(COMMENT_API_URL_PATH + "/" + VALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(messageResponseDTO.getMessage())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();
        commentDTO.setText(null);

        // then
        mockMvc.perform(post(COMMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();
        commentDTO.setText(null);

        // then
        mockMvc.perform(put(COMMENT_API_URL_PATH + "/" + VALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        when(commentService.findById(VALID_COMMENT_ID)).thenReturn(commentDTO);

        // then
        mockMvc.perform(get(COMMENT_API_URL_PATH + "/" + VALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDTO.getText())))
                .andExpect(jsonPath("$.date", is(commentDTO.getDate())))
                .andExpect(jsonPath("$.author.firstName", is(commentDTO.getAuthor().getFirstName())))
                .andExpect(jsonPath("$.post.body", is(commentDTO.getPost().getBody())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        doThrow(CommentNotFoundException.class).when(commentService).findById(INVALID_COMMENT_ID);

        // then
        mockMvc.perform(get(COMMENT_API_URL_PATH + "/" + INVALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithCommentIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CommentDTO commentDTO = CommentDTOBuilder.builder().build().toCommentDTO();

        // when
        when(commentService.listAll()).thenReturn(Collections.singletonList(commentDTO));

        // then
        mockMvc.perform(get(COMMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text", is(commentDTO.getText())))
                .andExpect(jsonPath("$[0].date", is(commentDTO.getDate())))
                .andExpect(jsonPath("$[0].author.firstName", is(commentDTO.getAuthor().getFirstName())))
                .andExpect(jsonPath("$[0].post.body", is(commentDTO.getPost().getBody())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(commentService).delete(VALID_COMMENT_ID);

        // then
        mockMvc.perform(delete(COMMENT_API_URL_PATH + "/" + VALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(CommentNotFoundException.class).when(commentService).delete(INVALID_COMMENT_ID);

        // then
        mockMvc.perform(delete(COMMENT_API_URL_PATH + "/" + INVALID_COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MessageResponseDTO createExpectedSuccessMessage(CommentDTO expectedSavedComment) {
        return MessageResponseDTO
                .builder()
                .message("Created comment with ID " + expectedSavedComment.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(CommentDTO expectedUpdatedComment) {
        return MessageResponseDTO
                .builder()
                .message("Updated comment with ID " + expectedUpdatedComment.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(PostDTO expectedUpdatedComment) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Comment with ID %s not found!", expectedUpdatedComment.getId()))
                .build();
    }
}
