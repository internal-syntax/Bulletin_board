package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.User;

import java.io.IOException;

public interface UserService {
    UserDto getUser(Authentication auth);
    UpdateUserDto updateUser(Authentication auth, UpdateUserDto user);
    boolean updatePassword(Authentication auth, NewPasswordDto password);
    boolean updateAvatar(Authentication auth, MultipartFile image) throws IOException;
    byte[] getAvatar(int idUser);
}
