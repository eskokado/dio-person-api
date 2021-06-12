package com.dio.diopersonapi.repositories;

import com.dio.diopersonapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
