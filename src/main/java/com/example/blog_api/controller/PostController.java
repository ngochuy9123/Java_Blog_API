package com.example.blog_api.controller;


import com.example.blog_api.payload.PostDto;
import com.example.blog_api.payload.PostResponse;
import com.example.blog_api.service.PostService;
import com.example.blog_api.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir){
        return postService.getAllPosts(pageNo, pageSize,sortBy,sortDir);
    }

    @GetMapping("/query")
    public PostDto findPostById(@RequestParam long postId){
        return postService.findPostById(postId);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto>  updatePost(@Valid @RequestBody PostDto postDto,@PathVariable("postId") long postId){
        PostDto postResponse = postService.updatePost(postDto,postId);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId")long postId){
        postService.deletePostById(postId);
        return new ResponseEntity<>("Post deleted successful", HttpStatus.OK);
    }
}
