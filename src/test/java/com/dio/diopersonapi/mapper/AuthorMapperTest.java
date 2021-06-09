package com.dio.diopersonapi.mapper;

import com.dio.diopersonapi.dto.mapper.AuthorMapper;
import com.dio.diopersonapi.dto.request.AuthorDTO;
import com.dio.diopersonapi.entities.Person;
import com.dio.diopersonapi.utils.AuthorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthorMapperTest {
    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void testGivenAuthorDTOThenReturnPersonEntity() {
        AuthorDTO authorDTO = AuthorUtils.createFakeDTO();
        Person person = authorMapper.toModel(authorDTO);

        assertEquals(authorDTO.getFirstName(), person.getFirstName());
        assertEquals(authorDTO.getLastName(), person.getLastName());
    }

    @Test
    void testGivenPersonEntityThenReturnPersonDTO() {
        Person person = AuthorUtils.createFakeEntity();
        AuthorDTO authorDTO = authorMapper.toDTO(person);

        assertEquals(person.getFirstName(), authorDTO.getFirstName());
        assertEquals(person.getLastName(), authorDTO.getLastName());
    }

}
