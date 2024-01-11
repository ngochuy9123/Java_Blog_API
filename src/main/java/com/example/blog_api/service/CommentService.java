package com.example.blog_api.service;


import com.example.blog_api.entity.Comment;
import com.example.blog_api.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto);
    List<CommentDto> getCommentByPostId(long postId);
    CommentDto getCommentById(long postId,long commentId);
    CommentDto updateComment(long postId,long commentId,CommentDto commentRequest);
    void deleteComment(long postId,long commentId);
}
