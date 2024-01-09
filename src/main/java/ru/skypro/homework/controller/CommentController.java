package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.Validation;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Комментарии")
@RequestMapping("ads")
public class CommentController {

    private final CommentService commentService;

    private final Validation validation;

    // Получение комментариев объявления
    @GetMapping("/{adId}/comments")
    public ResponseEntity<Object> getComments(@PathVariable Integer adId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CommentsDto commentsDto = commentService.getComments(auth, adId);
        if (commentsDto.getCount() == 0){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(commentsDto);
        }
    }

    // Добавление комментария к объявлению
    @PostMapping("/{adId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CommentDto commentDto = commentService.addComment(auth, adId, comment);
        return ResponseEntity.ok(commentDto);
    }

    // Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@validationImpl.validateComment(authentication,#commentId)")
    public ResponseEntity<String> delComment(@PathVariable Integer adId,
                                             @PathVariable Integer commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (commentService.deleteComment(auth, adId, commentId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Изменение комментария
    @PreAuthorize("@validationImpl.validateComment(authentication,#commentId)")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(commentService.updateComment(auth, adId, commentId, comment));
    }

}
