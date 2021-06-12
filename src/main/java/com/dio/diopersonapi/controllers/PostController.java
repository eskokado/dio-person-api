package com.dio.diopersonapi.controllers;

import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.exception.PostNotFoundException;
import com.dio.diopersonapi.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PostController {

    private PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createPost(@RequestBody @Valid PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @GetMapping
    public List<PostDTO> listAll() {
        return postService.listAll();
    }

    @GetMapping("/{id}")
    public PostDTO findById(@PathVariable Long id) throws PostNotFoundException {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO updateById(
            @PathVariable Long id, @RequestBody @Valid PostDTO postDTO)
            throws PostNotFoundException {
        return postService.updateById(id, postDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws PostNotFoundException {
        postService.delete(id);
    }
}
