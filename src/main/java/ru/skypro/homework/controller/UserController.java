package ru.skypro.homework.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    private final UserServiceImpl userService;

//    public UserController(UserServiceImpl userService) {
//        this.userService = userService;
//    }

    // Метод для обновления пароля
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPasswordDto newPasswordDto) {
//        userService.setPassword(newPasswordDto);
        return ResponseEntity.ok().build();
    }

    // Метод для получения информации об авторизованном пользователе
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser() {
//        UserDto currentUserDto = userService.getCurrentUser();
//        return ResponseEntity.ok(currentUserDto);
        return ResponseEntity.ok(new UserDto());
    }

    // Метод для обновления информации об авторизованном пользователе
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserDto updateUserDto) {
//        UserDto updatedUserDto = userService.updateUser(updateUserDto);
//        return ResponseEntity.ok(updatedUserDto);
        return ResponseEntity.ok(new UserDto());
    }

    // Метод для обновления аватара авторизованного пользователя
    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image) {
//        userService.updateUserImage(image);
        return ResponseEntity.ok().build();
    }
}
