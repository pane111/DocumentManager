package com.fhtw.rest.service;

import com.fhtw.rest.model.Comment;
import com.fhtw.rest.repository.CommentRepo;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class CommentService {
    private final CommentRepo commentRepo;
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public List<Comment> getCommentsByDocId(Long docId) {
        return commentRepo.findByDocId(docId);
    }

    public Comment createComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public List<Comment> findAll() {
        return commentRepo.findAll();
    }
}
