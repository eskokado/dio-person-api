package com.dio.diopersonapi.controllers;

import com.dio.diopersonapi.builders.PersonDTOBuilder;
import com.dio.diopersonapi.dto.request.PersonDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.exception.PersonNotFoundException;
import com.dio.diopersonapi.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
public class PersonControllerTest {
    private static final String PERSON_API_URL_PATH = "/api/v1/persons";
    private static final long VALID_PERSON_ID = 1L;
    private static final long INVALID_PERSON_ID = 2l;

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAPersonIsCreated() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        // when
        MessageResponseDTO expectedSuccessMessage = createExpectedSuccessMessage(personDTO);
        when(personService.createPerson(personDTO)).thenReturn(expectedSuccessMessage);

        // then
        mockMvc.perform(post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedSuccessMessage.getMessage())));
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        doThrow(PersonNotFoundException.class).when(personService).updateById(INVALID_PERSON_ID, personDTO);

        // then
        mockMvc.perform(put(PERSON_API_URL_PATH + "/" + INVALID_PERSON_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        // when
        when(personService.updateById(VALID_PERSON_ID, personDTO)).thenReturn(updateExpectedSuccessMessage(personDTO));

        // then
        mockMvc.perform(put(PERSON_API_URL_PATH + "/" + VALID_PERSON_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(updateExpectedSuccessMessage(personDTO).getMessage())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        personDTO.setCpf(null);

        // then
        mockMvc.perform(post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.findById(personDTO.getId())).thenReturn(personDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH + "/" + personDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(personDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDTO.getLastName())))
                .andExpect(jsonPath("$.phones[0].number", is(personDTO.getPhones().get(0).getNumber())))
                .andExpect(jsonPath("$.cpf", is(personDTO.getCpf())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.findById(personDTO.getId())).thenThrow(PersonNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH + "/" + personDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithPersonsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.listAll()).thenReturn(Collections.singletonList(personDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is(personDTO.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(personDTO.getLastName())))
                .andExpect(jsonPath("$[0].cpf", is(personDTO.getCpf())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        doNothing().when(personService).delete(personDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PERSON_API_URL_PATH + "/" + personDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private MessageResponseDTO createExpectedSuccessMessage(PersonDTO expectedSavedPerson) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID " + expectedSavedPerson.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(PersonDTO expectedUpdatedPerson) {
        return MessageResponseDTO
                .builder()
                .message("Updated person with ID " + expectedUpdatedPerson.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(PersonDTO expectedUpdatedPerson) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Person with ID %s not found!", expectedUpdatedPerson.getId()))
                .build();
    }
}
