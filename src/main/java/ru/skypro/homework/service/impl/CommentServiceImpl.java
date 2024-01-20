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

/**
 * Сервис для управления комментариями к объявлениям.
 *
 * @author КараваевАВ
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final Validation validation;

    /**
     * Добавить новый комментарий к объявлению.
     *
     * @param auth          Детали аутентификации.
     * @param adId          Идентификатор объявления.
     * @param crOrUpdComDto DTO с информацией для создания или обновления комментария.
     * @return DTO нового комментария.
     * @throws UserNotFoundException   обработка исключения если пользователь не найден.
     * @throws EntityNotFoundException обработка исключения если объявление не найдено.
     */
    @Override
    public CommentDto addComment(Authentication auth, int adId, CreateOrUpdateCommentDto crOrUpdComDto) {
        log.debug("--- выполнение метода сервиса addComment");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + auth.getName()));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));
        Comment newComment = commentMapper.inDto(user, ad, crOrUpdComDto);
        return commentMapper.outDto(commentRepository.save(newComment));
    }

    /**
     * Получить список комментариев к объявлению.
     *
     * @param auth Детали аутентификации.
     * @param adId Идентификатор объявления.
     * @return DTO списка комментариев.
     */
    @Override
    public CommentsDto getComments(Authentication auth, int adId) {
        log.info("--- выполнение метода сервиса getComments");
        List<Comment> commentList = commentRepository.findCommentsByAd_Id(adId);
        return commentMapper.outDtos(commentList);
    }

    /**
     * Обновить комментарий к объявлению.
     *
     * @param auth          Детали аутентификации.
     * @param adId          Идентификатор объявления.
     * @param commentId     Идентификатор комментария.
     * @param crOrUpdComDto DTO с информацией для обновлении комментария.
     * @return Обновленый DTO комментария.
     * @throws EntityNotFoundException обработка исключения если объявление не найдено.
     * @throws EntityNotFoundException обработка исключения если комментарий не найден.
     */
    @Override
    public CommentDto updateComment(Authentication auth, int adId, int commentId, CreateOrUpdateCommentDto crOrUpdComDto) {
        log.debug("--- выполнение метода сервиса updateComment");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));

        Comment currentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден по идентификатору: " + commentId));

        if (validation.validateComment(auth, commentId)) {
            Comment newComment = commentMapper.inDto(currentComment.getUser(), ad, crOrUpdComDto);
            newComment.setId(currentComment.getId());
            return commentMapper.outDto(commentRepository.save(newComment));
        } else {
            throw new UnauthorizedUserException("Пользователь не авторизован для изменения комментария");
        }
    }

    /**
     * Удаление комментария к объявлению.
     *
     * @param auth      Детали аутентификации.
     * @param adId      Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @return True, если комментарий успешно удален, в противном случае — false.
     * @throws CommentNotFoundException обработка исключения если комментарий не найден.
     * @throws EntityNotFoundException  обработка исключения если объявление не найдено.
     */
    @Override
    public boolean deleteComment(Authentication auth, int adId, int commentId) {
        log.debug("--- выполнение метода сервиса deleteComment");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден по идентификатору: " + commentId));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));

        if (comment.getAd().equals(ad) && validation.validateComment(auth, commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
