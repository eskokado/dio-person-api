package com.dio.diopersonapi.repositories;

import com.dio.diopersonapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
