package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@Tag(name = "Пользователи")
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Изменение пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пароль успешно изменен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewPasswordDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка изменения пароля",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewPasswordDto.class)
                            )
                    )
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody NewPasswordDto pass) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.updatePassword(auth, pass)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Авторизованный пользователь получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Авторизованный пользователь не получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    )
            }
    )
    @GetMapping("/me")
    public UserDto getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth);
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные пользователя успешно обновлены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Произошла ошибка при обновлении данных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    )
            }
    )
    @PatchMapping("/me")
    public UpdateUserDto updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.updateUser(auth, updateUserDto);
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Аватар пользователя успешно обновлен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка обновления аватара пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    )
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestPart("image") MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.updateAvatar(auth, image);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение аватара пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Аватар пользователя успешно получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка получения аватара пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    )
            }
    )
    @GetMapping("/avatars/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Integer avatarId) {
        return ResponseEntity.ok().body(userService.getAvatar(avatarId));
    }
}
