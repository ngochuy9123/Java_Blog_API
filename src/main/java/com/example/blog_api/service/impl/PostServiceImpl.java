package com.example.blog_api.service.impl;

import com.example.blog_api.entity.Post;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.payload.PostDto;
import com.example.blog_api.payload.PostResponse;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);

        Post newPost = postRepository.save(post);

        return mapToDto(newPost);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> lstPost = posts.getContent();
        List<PostDto> contents = lstPost.stream().map(this::mapToDto).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto findPostById(long idPost) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new ResourceNotFoundException("Post","id",String.valueOf(idPost)));

        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));
        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post){
//        PostDto postDto = new PostDto();
//
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        PostDto postDto = modelMapper.map(post,PostDto.class);

        return postDto;
    }

    private Post mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());
        Post post = modelMapper.map(postDto,Post.class);
        return post;
    }
}
