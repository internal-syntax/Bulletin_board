package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Authentication auth, int adId, CreateOrUpdateCommentDto crOrUpdAdDto);
    CommentsDto getComments(Authentication auth, int adId);
    CommentDto updateComment(Authentication auth, int adId,int commentId, CreateOrUpdateCommentDto crOrUpdAdDto);
    boolean deleteComment(Authentication auth,int adId, int commentId);
}
