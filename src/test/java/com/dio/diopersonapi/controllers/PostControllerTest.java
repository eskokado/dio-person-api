package com.dio.diopersonapi.controllers;

import com.dio.diopersonapi.builders.PostDTOBuilder;
import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.exception.PostNotFoundException;
import com.dio.diopersonapi.services.PostService;
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
public class PostControllerTest {

    private static final String POST_API_URL_PATH = "/api/v1/posts";
    private static final long VALID_POST_ID = 1L;
    private static final long INVALID_POST_ID = 2l;

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAPostIsCreated() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        MessageResponseDTO messageResponseDTO = createExpectedSuccessMessage(postDTO);
        when(postService.createPost(postDTO)).thenReturn(messageResponseDTO);

        // then
        mockMvc.perform(post(POST_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(messageResponseDTO.getMessage())));
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        doThrow(PostNotFoundException.class).when(postService).updateById(INVALID_POST_ID, postDTO);

        // then
        mockMvc.perform(put(POST_API_URL_PATH + "/" + INVALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        when(postService.updateById(VALID_POST_ID, postDTO)).thenReturn(updateExpectedSuccessMessage(postDTO));

        // then
        mockMvc.perform(put(POST_API_URL_PATH + "/" + VALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(updateExpectedSuccessMessage(postDTO).getMessage())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();
        postDTO.setBody(null);

        // then
        mockMvc.perform(post(POST_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();
        postDTO.setBody(null);

        // then
        mockMvc.perform(put(POST_API_URL_PATH + "/" + VALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        when(postService.findById(VALID_POST_ID)).thenReturn(postDTO);

        // then
        mockMvc.perform(get(POST_API_URL_PATH + "/" + VALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(postDTO.getTitle())))
                .andExpect(jsonPath("$.body", is(postDTO.getBody())))
                .andExpect(jsonPath("$.date", is(postDTO.getDate())))
                .andExpect(jsonPath("$.author.firstName", is(postDTO.getAuthor().getFirstName())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        doThrow(PostNotFoundException.class).when(postService).findById(INVALID_POST_ID);

        // then
        mockMvc.perform(get(POST_API_URL_PATH + "/" + INVALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithPostIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PostDTO postDTO = PostDTOBuilder.builder().build().toPostDTO();

        // when
        when(postService.listAll()).thenReturn(Collections.singletonList(postDTO));

        // then
        mockMvc.perform(get(POST_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(postDTO.getTitle())))
                .andExpect(jsonPath("$[0].body", is(postDTO.getBody())))
                .andExpect(jsonPath("$[0].date", is(postDTO.getDate())))
                .andExpect(jsonPath("$[0].author.firstName", is(postDTO.getAuthor().getFirstName())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(postService).delete(VALID_POST_ID);

        // then
        mockMvc.perform(delete(POST_API_URL_PATH + "/" + VALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(PostNotFoundException.class).when(postService).delete(INVALID_POST_ID);

        // then
        mockMvc.perform(delete(POST_API_URL_PATH + "/" + INVALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MessageResponseDTO createExpectedSuccessMessage(PostDTO expectedSavedPost) {
        return MessageResponseDTO
                .builder()
                .message("Created post with ID " + expectedSavedPost.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(PostDTO expectedUpdatedPost) {
        return MessageResponseDTO
                .builder()
                .message("Updated post with ID " + expectedUpdatedPost.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(PostDTO expectedUpdatedPost) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Post with ID %s not found!", expectedUpdatedPost.getId()))
                .build();
    }
}
