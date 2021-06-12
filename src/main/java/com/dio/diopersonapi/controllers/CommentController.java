package com.dio.diopersonapi.controllers;

import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.exception.CommentNotFoundException;
import com.dio.diopersonapi.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

    private CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createComment(@RequestBody @Valid CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }

    @GetMapping
    public List<CommentDTO> listAll() {
        return commentService.listAll();
    }

    @GetMapping("/{id}")
    public CommentDTO findById(@PathVariable Long id) throws CommentNotFoundException {
        return commentService.findById(id);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO updateById(
            @PathVariable Long id, @RequestBody @Valid CommentDTO commentDTO)
            throws CommentNotFoundException {
        return commentService.updateById(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws CommentNotFoundException {
        commentService.delete(id);
    }
}
