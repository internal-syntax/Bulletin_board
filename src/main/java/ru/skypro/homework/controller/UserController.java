package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Пользователи")
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    // Изменение пароля
    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody NewPasswordDto pass) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.updatePassword(auth, pass)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Получение информации об авторизованном пользователе
    @GetMapping("/me")
    public UserDto getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth);
    }

    // Обновление информации об авторизованном пользователе
    @PatchMapping("/me")
    public UpdateUserDto updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.updateUser(auth, updateUserDto);
    }

    // Обновление аватара авторизованного пользователя
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestPart("image") MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.updateAvatar(auth, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatars/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Integer avatarId) {
        return ResponseEntity.ok().body(userService.getAvatar(avatarId));
    }
}
