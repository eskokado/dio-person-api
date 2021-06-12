package com.dio.diopersonapi.dto.mapper;

import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd")
    Comment toModel(CommentDTO commentDTO);

    CommentDTO toDTO(Comment comment);
}
