package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.service.Validation;


/**
 * Сервис проверки разрешений на редактирование комментариев и объявления.
 *
 * <p>Эта служба предоставляет методы проверки того, имеет ли пользователь право
 * для редактирования комментария или объявления.</p>
 *
 * @author КараваевАВ
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationImpl implements Validation {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    /**
     * Проверка прав пользователя на редактирование комментария.
     *
     * @param auth Детали аутентификации.
     * @param commentId Идентификатор комментария.
     * @return True, если у пользователя есть права на редактирование, в противном случае — false.
     */
    @Override
    public boolean validateComment(Authentication auth, int commentId) {
        if (!commentRepository.existsById(commentId)){
            return false;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = comment.getUser();
        return isUserAllowedToEdit(auth, user);
    }

    /**
     * Проверка прав пользователя на редактирование объявления.
     *
     * @param auth Детали аутентификации.
     * @param adId Идентификатор объявления.
     * @return True, если у пользователя есть права на редактирование, в противном случае — false.
     */
    @Override
    public boolean validateAd(Authentication auth,int adId) {
        if (!adRepository.existsById(adId)){
            return false;
        }
        Ad ad = adRepository.findById(adId).orElseThrow();
        User user = ad.getUser();
        return isUserAllowedToEdit(auth, user);
    }

    /**
     * Проверка, разрешено ли пользователю редактирование в зависимости от его роли и прав.
     *
     * @param auth Детали аутентификации.
     * @param user User, представляющий владельца объекта.
     * @return True, если у пользователя есть права на редактирование, в противном случае — false.
     */
    private boolean isUserAllowedToEdit(Authentication auth, User user) {
        log.debug("Auth username: {}", auth.getName());
        log.debug("User username: {}", user.getLogin());
        return user.getLogin().equals(auth.getName())
                || auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
