package com.dio.diopersonapi.utils;

import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.entities.Comment;

import java.time.LocalDate;

public class CommentUtils {

    private static final String TEXT = "Bom dia! Tudo bem?";
    private static final long COMMENT_ID = 1L;
    public static final LocalDate DATE = LocalDate.of(2021, 6, 9);

    public static CommentDTO createFakeDTO() {
        return CommentDTO.builder()
                .text(TEXT)
                .date("09-06-2021")
                .author(AuthorUtils.createFakeDTO())
                .build();
    }

    public static Comment createFakeEntity() {
        return Comment.builder()
                .id(COMMENT_ID)
                .text(TEXT)
                .date(DATE)
                .author(AuthorUtils.createFakeEntity())
                .build();
    }

}