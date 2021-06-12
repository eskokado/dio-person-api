package com.dio.diopersonapi.services;

import com.dio.diopersonapi.dto.mapper.PostMapper;
import com.dio.diopersonapi.dto.request.PostDTO;
import com.dio.diopersonapi.dto.response.MessageResponseDTO;
import com.dio.diopersonapi.entities.Post;
import com.dio.diopersonapi.exception.PostNotFoundException;
import com.dio.diopersonapi.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PostService {

    private PostRepository postRepository;

    private final PostMapper postMapper = PostMapper.INSTANCE;

    public MessageResponseDTO createPost(PostDTO postDTO) {
        Post postToSave = postMapper.toModel(postDTO);
        Post createdPost = postRepository.save(postToSave);
        return createMessageResponse(createdPost.getId(), "Created post with ID ");
    }

    public MessageResponseDTO updateById(Long id, PostDTO postDTO) throws PostNotFoundException {
        verifyIfExists(id);
        Post postToUpdate = postMapper.toModel(postDTO);
        Post updatedPost = postRepository.save(postToUpdate);
        return createMessageResponse(updatedPost.getId(), "Updated post with ID ");
    }

    public List<PostDTO> listAll() {
        List<Post> allPeople = postRepository.findAll();
        return allPeople.stream().map(postMapper::toDTO).collect(Collectors.toList());
    }

    public PostDTO findById(Long id) throws PostNotFoundException {
        Post post = verifyIfExists(id);
        return postMapper.toDTO(post);
    }

    public void delete(Long id) throws PostNotFoundException {
        verifyIfExists(id);
        postRepository.deleteById(id);
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }

    public Post verifyIfExists(Long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

}
