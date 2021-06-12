package com.dio.diopersonapi.services;

import com.dio.diopersonapi.dto.mapper.CommentMapper;
import com.dio.diopersonapi.dto.request.CommentDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.entities.Comment;
import com.dio.diopersonapi.exception.CommentNotFoundException;
import com.dio.diopersonapi.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommentService {

    private CommentRepository commentRepository;

    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    public MessageResponseDTO createComment(CommentDTO commentDTO) {
        Comment commentToSave = commentMapper.toModel(commentDTO);
        Comment createdComment = commentRepository.save(commentToSave);
        return createMessageResponse(createdComment.getId(), "Created comment with ID ");
    }

    public MessageResponseDTO updateById(Long id, CommentDTO commentDTO) throws CommentNotFoundException {
        verifyIfExists(id);
        Comment commentToUpdate = commentMapper.toModel(commentDTO);
        Comment updatedComment = commentRepository.save(commentToUpdate);
        return createMessageResponse(updatedComment.getId(), "Updated comment with ID ");
    }

    public List<CommentDTO> listAll() {
        List<Comment> allPeople = commentRepository.findAll();
        return allPeople.stream().map(commentMapper::toDTO).collect(Collectors.toList());
    }

    public CommentDTO findById(Long id) throws CommentNotFoundException {
        Comment comment = verifyIfExists(id);
        return commentMapper.toDTO(comment);
    }

    public void delete(Long id) throws CommentNotFoundException {
        verifyIfExists(id);
        commentRepository.deleteById(id);
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }

    public Comment verifyIfExists(Long id) throws CommentNotFoundException {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

}
