package com.example.ChatApp.service;


import com.example.ChatApp.dto.PostDto;
import com.example.ChatApp.dto.PostUpdateDto;
import com.example.ChatApp.entity.PostEntity;
import com.example.ChatApp.entity.UserEntity;
import com.example.ChatApp.exception.ResourceNotFoundException;
import com.example.ChatApp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto addPost(PostDto body) {
        PostEntity post= modelMapper.map(body,PostEntity.class);
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(user);
        return modelMapper.map(postRepository.save(post),PostDto.class);

    }

    public PostDto getPost(Long id) {

        PostEntity post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Request Post Not Found ."));
      return modelMapper.map(post, PostDto.class);
    }

    public PostDto updatePost(Long id, PostUpdateDto body) {

        PostEntity post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Request Post Not Found ."));

        UserEntity loggedInUser=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!post.getAuthor().getEmail().equals(loggedInUser.getEmail())){
            throw new AuthorizationDeniedException("User is not admin of this post");
        }

        body.setId(post.getId());
        modelMapper.map(body,post);
        postRepository.save(post);

        return modelMapper.map(post,PostDto.class);

    }
}
