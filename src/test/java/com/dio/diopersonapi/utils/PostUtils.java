package com.dio.diopersonapi.utils;

import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.entities.Post;

import java.time.LocalDate;
import java.util.Collections;

public class PostUtils {

    private static final String TITLE = "clean architecture";
    private static final String BODY = "Uma forma de deixar o código limpo e aplicar SOLID";
    private static final long POST_ID = 1L;
    public static final LocalDate DATE = LocalDate.of(2021, 6, 9);

    public static PostDTO createFakeDTO() {
        return PostDTO.builder()
                .title(TITLE)
                .body(BODY)
                .date("2021-06-09")
                .author(AuthorUtils.createFakeDTO())
                .comments(Collections.singletonList(CommentUtils.createFakeDTO()))
                .build();
    }

    public static Post createFakeEntity() {
        return Post.builder()
                .id(POST_ID)
                .title(TITLE)
                .body(BODY)
                .date(DATE)
                .author(AuthorUtils.createFakeEntity())
                .comments(Collections.singletonList(CommentUtils.createFakeEntity()))
                .build();
    }
}
