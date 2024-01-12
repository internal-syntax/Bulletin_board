package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exceptions.*;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.Validation;

import java.time.LocalDateTime;
import java.util.*;

import static liquibase.repackaged.net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final Validation validation;

    @Override
    public CommentDto addComment(Authentication auth, int adId, CreateOrUpdateCommentDto crOrUpdComDto) {
        log.debug("--- выполнение метода сервиса addComment");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(()->new UserNotFoundException("Пользователь не найден: " + auth.getName()));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(()->new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));
        Comment newComment = commentMapper.inDto(user, ad, crOrUpdComDto);
        return commentMapper.outDto(commentRepository.save(newComment));
    }

    @Override
    public CommentsDto getComments(Authentication auth, int adId) {
        log.info("--- выполнение метода сервиса getComments");
        List<Comment> commentList = commentRepository.findCommentsByAd_Id(adId);
        return commentMapper.outDtos(commentList);
    }

    @Override
    public CommentDto updateComment(Authentication auth, int adId, int commentId, CreateOrUpdateCommentDto crOrUpdComDto) {
        log.debug("--- выполнение метода сервиса updateComment");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(()-> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));

        Comment currentComment = commentRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFoundException("Комментарий не найден по идентификатору: " + commentId));

        if(validation.validateComment(auth,commentId))
        {
            Comment newComment = commentMapper.inDto(currentComment.getUser(),ad,crOrUpdComDto);
            newComment.setId(currentComment.getId());
            return commentMapper.outDto(commentRepository.save(newComment));
        } else {
            throw new UnauthorizedUserException("Пользователь не авторизован для изменения комментария");
        }
    }

    @Override
    public boolean deleteComment(Authentication auth, int adId, int commentId) {
        log.debug("--- выполнение метода сервиса deleteComment");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CommentNotFoundException("Комментарий не найден по идентификатору: " + commentId));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(()-> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));

        if(comment.getAd().equals(ad) && validation.validateComment(auth,commentId)){
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
