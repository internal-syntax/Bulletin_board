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

/**
 * Сервис для обработки пользовательских операций.
 *
 * @author КараваевАВ
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    /**
     * Загрузка данных пользователя по имени пользователя.
     *
     * @param username Имя пользователя для загрузки сведений о пользователе.
     * @return UserDetails указанного пользователя.
     * @throws UsernameNotFoundException, обработка исключения если пользователь не найден.
     */
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

    /**
     * Получите информацию о пользователе на основе данных аутентификации.
     *
     * @param auth Детали аутентификации.
     *             Объект @return UserDto DTO, содержащий информацию о пользователе.
     * @throws UserNotFoundException, обработка исключения если пользователь не найден.
     */
    @Override
    public UserDto getUser(Authentication auth) {
        log.debug("--- выполнение метода сервиса getUser");
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден " + auth.getName()));
        return userMapper.outDto(user);
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param auth          Детали аутентификации.
     * @param updateUserDto DTO с обновленной информацией о пользователе.
     * @return Обновлен объект UpdateUserDto.
     * @throws UserNotFoundException, обработка исключения если пользователь не найден.
     */
    @Override
    public UpdateUserDto updateUser(Authentication auth, UpdateUserDto updateUserDto) {
        log.debug("--- выполнение метода сервиса updateUser");
        ru.skypro.homework.model.User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден " + auth.getName()));
        ru.skypro.homework.model.User updatedUser = userMapper.inDto(updateUserDto, user);
        userRepository.save(updatedUser);
        return updateUserDto;
    }

    /**
     * Обновить пароль пользователя.
     *
     * @param auth     Детали аутентификации.
     * @param password DTO с новыми и текущими паролями.
     * @return True, если пароль обновлен, в противном случае — false.
     */
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

    /**
     * Обновление аватара пользователя.
     *
     * @param auth  Детали аутентификации.
     * @param image Новые данные изображения аватара.
     * @return True, если аватар обновлен, в противном случае — false.
     * @throws IOException              при возникновении ошибки ввода-вывода.
     * @throws UserNotFoundException,   обработка исключения если пользователь не найден.
     * @throws AvatarNotFoundException, обработка исключения если аватар не найден.
     */
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

    /**
     * Получить данные аватара по идентификатору аватара.
     *
     * @param avatarId Идентификатор аватара.
     * @return Массив байтов, представляющий данные аватара.
     * @throws AvatarNotFoundException, обработка исключения если аватар не найден.
     */
    @Override
    public byte[] getAvatar(int avatarId) {
        log.info("--- выполнение метода сервиса getAvatar");
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new AvatarNotFoundException());
        return avatar.getData();
    }
}