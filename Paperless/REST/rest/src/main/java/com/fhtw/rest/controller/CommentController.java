package com.fhtw.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.rest.model.Comment;
import com.fhtw.rest.service.CommentService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/comments")
@Log
public class CommentController {

    private final CommentService service;
    public CommentController(CommentService service) {
        this.service = service;
    }
    @GetMapping
    public List<Comment> getAllComments()
    {
        log.info("Finding all comments");
        List<Comment> comments = service.findAll();
        for (Comment comment : comments) {
            log.info("Comment found: " + comment.getId() + ", on document " + comment.getDocId());
        }
        return comments;
    }


    @GetMapping("/{id}")
    public List<Comment> getCommentsByDocId(@PathVariable Long id)
    {
        log.info("Finding all comments of document with id: " + id);
        return service.getCommentsByDocId(id);
    }
    @PostMapping
    public ResponseEntity<?> createComment(@RequestParam Long doc_id, @RequestParam String author, @RequestParam String content)
    {
        log.info("Creating a new comment.");
        Comment comment = new Comment();
        comment.setDocId(doc_id);
        comment.setAuthor(author);
        comment.setContent(content);
        service.createComment(comment);
        return new ResponseEntity<>("Comment posted", HttpStatus.CREATED);
    }
}
