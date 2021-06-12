package com.dio.diopersonapi.services;

import com.dio.diopersonapi.dto.request.PersonDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.entities.Person;
import com.dio.diopersonapi.exception.PersonNotFoundException;
import com.dio.diopersonapi.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dio.diopersonapi.utils.PersonUtils.createFakeDTO;
import static com.dio.diopersonapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSuccessSavedMessage() {
        PersonDTO personDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        MessageResponseDTO expectedSuccessMessage = createExpectedSuccessMessage(expectedSavedPerson);
        MessageResponseDTO successMessage = personService.createPerson(personDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenPersonDTOThenReturnSuccessUpdatedMessage() throws PersonNotFoundException {
        PersonDTO personDTO = createFakeDTO();
        Person expectedUpdatedPerson = createFakeEntity();

        when(personRepository.save(any(Person.class))).thenReturn(expectedUpdatedPerson);
        when(personRepository.findById(expectedUpdatedPerson.getId())).thenReturn(Optional.of(expectedUpdatedPerson));

        MessageResponseDTO expectedSuccessMessage = updateExpectedSuccessMessage(expectedUpdatedPerson);
        MessageResponseDTO successMessage = personService.updateById(expectedUpdatedPerson.getId(), personDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenPersonDTOThenReturnPersonNotFoundExceptionMessage() {
        PersonDTO personDTO = createFakeDTO();
        Person expectedPerson = createFakeEntity();

        when(personRepository.findById(expectedPerson.getId())).thenReturn(Optional.empty());

        PersonNotFoundException exception =
                assertThrows(
                        PersonNotFoundException.class,
                        () -> personService.verifyIfExists(expectedPerson.getId()));

        assertEquals(notFoundMessage(expectedPerson).getMessage(), exception.getMessage());
    }

    @Test
    void testGivenPersonDTOThenReturnListPersonDTO(){
        Person expectedPerson = createFakeEntity();

        when(personRepository.findAll()).thenReturn(Collections.singletonList(expectedPerson));

        List<PersonDTO> successList = personService.listAll();

        assertTrue(successList.size() > 0);
    }

    @Test
    void testGivenDeleteByIdThenNotThrow() throws PersonNotFoundException {
        Person expectedPerson = createFakeEntity();

        when(personRepository.findById(expectedPerson.getId())).thenReturn(Optional.of(expectedPerson));

        personService.delete(expectedPerson.getId());

        verify(personRepository, times(1)).deleteById(expectedPerson.getId());
    }

    @Test
    void testGivenFindByIdThenReturnPersonDTO() throws PersonNotFoundException {
        PersonDTO expectedPersonDTO = createFakeDTO();
        Person expectedPerson = createFakeEntity();

        when(personRepository.findById(expectedPerson.getId())).thenReturn(Optional.of(expectedPerson));

        PersonDTO successDTO = personService.findById(expectedPerson.getId());

        assertNotNull(successDTO);
        assertEquals(expectedPersonDTO.getFirstName(), successDTO.getFirstName());
        assertEquals(expectedPersonDTO.getLastName(), successDTO.getLastName());
        assertEquals(expectedPersonDTO.getCpf(), successDTO.getCpf());
    }


    private MessageResponseDTO createExpectedSuccessMessage(Person expectedSavedPerson) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID " + expectedSavedPerson.getId())
                .build();
    }

    private MessageResponseDTO updateExpectedSuccessMessage(Person expectedUpdatedPerson) {
        return MessageResponseDTO
                .builder()
                .message("Updated person with ID " + expectedUpdatedPerson.getId())
                .build();
    }

    private MessageResponseDTO notFoundMessage(Person expectedUpdatedPerson) {
        return MessageResponseDTO
                .builder()
                .message(String.format("Person with ID %s not found!", expectedUpdatedPerson.getId()))
                .build();
    }
}
