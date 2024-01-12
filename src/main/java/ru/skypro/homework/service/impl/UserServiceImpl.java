package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exceptions.AvatarNotFoundException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Логин не найден"));
        UserDetails userDetails = User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        return userDetails;
    }

    @Override
    public UserDto getUser(Authentication auth) {
        log.debug("--- выполнение метода сервиса getUser");
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден " + auth.getName()));
        return userMapper.outDto(user);
    }

    @Override
    public UpdateUserDto updateUser(Authentication auth, UpdateUserDto updateUserDto) {
        log.debug("--- выполнение метода сервиса updateUser");
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден " + auth.getName()));
        ru.skypro.homework.model.User updatedUser = userMapper.inDto(updateUserDto, user);
        userRepository.save(updatedUser);
        return updateUserDto;
    }

    @Override
    public boolean updatePassword(Authentication auth, NewPasswordDto password) {
        log.debug("--- выполнение метода сервиса updatePassword");
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName()).orElseThrow();

        if (encoder.matches(password.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(password.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAvatar(Authentication auth, MultipartFile image) throws IOException {
        log.debug("--- выполнение метода сервиса updateAvatar");
        try {
            ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден" + auth.getName()));

            Avatar avatar = user.getAvatar();
            avatar.setData(image.getBytes());
            user.setAvatar(avatar);
            userRepository.save(user);
            return true;
        } catch (IOException e) {
            log.error("Ошибка изменения аватара: " + e.getMessage());
            throw new AvatarNotFoundException();
        }
    }

    @Override
    public byte[] getAvatar(int avatarId) {
        log.info("--- выполнение метода сервиса getAvatar");
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new AvatarNotFoundException());
        return avatar.getData();
    }
}