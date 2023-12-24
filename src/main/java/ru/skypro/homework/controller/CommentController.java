package ru.skypro.homework.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.*;

@RestController
@RequestMapping("/api/ads/{adId}/comments")
public class CommentController {

//    private final CommentServiceImpl commentService;

//    public CommentController(CommentServiceImpl commentService) {
//        this.commentService = commentService;
//    }

    // Метод для получения комментариев к объявлению
    @GetMapping
    public ResponseEntity<CommentsDto> getComments(@PathVariable int adId) {
//        CommentsDto commentsDto = commentService.getComments(adId);
//        return ResponseEntity.ok(commentsDto);
        return ResponseEntity.ok(new CommentsDto());
    }

    // Метод для добавления комментария к объявлению
    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int adId,
            @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
//        CommentDto newCommentDto = commentService.addComment(adId, createOrUpdateCommentDto);
//        return ResponseEntity.status(201).body(newCommentDto);
        return ResponseEntity.ok(new CommentDto());
    }

    // Метод для удаления комментария
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable int adId,
            @PathVariable int commentId) {
//        commentService.deleteComment(adId, commentId);
//        return ResponseEntity.ok().build();
        return ResponseEntity.ok().build();
    }

    // Метод для обновления комментария
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable int adId,
            @PathVariable int commentId,
            @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
//        CommentDto updatedCommentDto = commentService.updateComment(adId, commentId, createOrUpdateCommentDto);
//        return ResponseEntity.ok(updatedCommentDto);
        return ResponseEntity.ok(new CommentDto());
    }

}
