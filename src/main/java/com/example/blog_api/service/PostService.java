package com.example.blog_api.service;

import com.example.blog_api.entity.Post;
import com.example.blog_api.payload.PostDto;
import com.example.blog_api.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy, String sortDir);
    PostDto findPostById(long idPost);
    PostDto updatePost(PostDto postDto, long id);
    void deletePostById(long id);
}
