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



@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationImpl implements Validation {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    @Override
    public boolean validateComment(Authentication auth, int commentId) {
        if (!commentRepository.existsById(commentId)){
            return false;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = comment.getUser();
        return isUserAllowedToEdit(auth, user);
    }

    @Override
    public boolean validateAd(Authentication auth,int adId) {
        if (!adRepository.existsById(adId)){
            return false;
        }
        Ad ad = adRepository.findById(adId).orElseThrow();
        User user = ad.getUser();
        return isUserAllowedToEdit(auth, user);
    }

    private boolean isUserAllowedToEdit(Authentication auth, User user) {
        log.debug("Auth username: {}", auth.getName());
        log.debug("User username: {}", user.getLogin());
        return user.getLogin().equals(auth.getName())
                || auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
