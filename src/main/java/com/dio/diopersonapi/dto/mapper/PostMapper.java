package com.dio.diopersonapi.dto.mapper;

import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd")
    Post toModel(PostDTO postDTO);

    PostDTO toDTO(Post post);
}
