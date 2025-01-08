package com.example.ChatApp.controller;


import com.example.ChatApp.dto.PostDto;
import com.example.ChatApp.dto.PostUpdateDto;
import com.example.ChatApp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/add")
    // @Secured("ROLE_USER")
    @PreAuthorize("hasAuthority('CREATE_POST') OR hasRole('ADMIN')")
    public PostDto addPost(@RequestBody PostDto body){

    return postService.addPost(body);
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PatchMapping("/update/{id}")
    public PostDto updatePost(@PathVariable Long id,@RequestBody PostUpdateDto body){
        return postService.updatePost(id,body);
    }

}
