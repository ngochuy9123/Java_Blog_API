package com.example.blog_api.controller;

import com.example.blog_api.entity.Comment;
import com.example.blog_api.payload.CommentDto;
import com.example.blog_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId, @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable( value = "postId") long postId){
        return commentService.getCommentByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long postId, @PathVariable long commentId){
        return new ResponseEntity<>(commentService.getCommentById(postId,commentId), HttpStatus.OK) ;
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long postId,@PathVariable long commentId,@Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(postId,commentId,commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long postId,@PathVariable long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment delete successfull", HttpStatus.OK);
    }
}
