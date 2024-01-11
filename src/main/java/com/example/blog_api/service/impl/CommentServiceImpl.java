package com.example.blog_api.service.impl;

import com.example.blog_api.entity.Comment;
import com.example.blog_api.entity.Post;
import com.example.blog_api.exception.BlogAPIException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.payload.CommentDto;
import com.example.blog_api.repository.CommentRepository;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }


    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",String.valueOf(postId)));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return null;
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment cmt:comments
             ) {
            commentDtos.add(mapToDto(cmt));
        }

        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",String.valueOf(postId)));

       Comment comment = commentRepository.findById(commentId).orElseThrow(
               ()->new ResourceNotFoundException("Comment","Id",String.valueOf(commentId)));

       if (!comment.getPost().getId().equals(post.getId())){
           throw new BlogAPIException(HttpStatus.BAD_REQUEST,"This comment does not belong this post");
       }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",String.valueOf(postId))
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment","Id",String.valueOf(commentId)));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"This comment does not belong this post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId,long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",String.valueOf(postId))
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment","Id",String.valueOf(commentId)));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"This comment does not belong this post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
