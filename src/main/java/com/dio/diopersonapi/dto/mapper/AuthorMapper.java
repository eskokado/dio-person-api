package com.dio.diopersonapi.dto.mapper;

import com.dio.diopersonapi.dto.request.AuthorDTO;
import com.dio.diopersonapi.entities.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Person toModel(AuthorDTO authorDTO);

    AuthorDTO toDTO(Person person);

}
