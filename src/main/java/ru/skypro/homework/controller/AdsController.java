package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.AdImage;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    @Operation(
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Все объявления получены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления не получены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление успешно создано",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Не удалось создать объявление",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") CreateOrUpdateAdDto crOrUpdAdDto,
                                       @RequestPart("image") MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdDto adDto = adService.addAd(auth, crOrUpdAdDto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(adDto);
    }

    @Operation(
            summary = "Получение объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление получено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявление не найдено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAdDto.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}")
    public ExtendedAdDto getAd(@PathVariable Integer id) {
        ExtendedAdDto extendedAdDto = adService.getAd(id);
        return extendedAdDto;
    }

    @Operation(
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление успешно удалено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Не удалось удалить объявление",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    )
            }
    )
    @PreAuthorize("@validationImpl.validateAd(authentication,#id)")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (adService.deleteAd(auth, id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Изменение объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление успешно обновлено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Не удалось обновить объявление",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    )
            }
    )
    @PreAuthorize("@validationImpl.validateAd(authentication,#id)")
    @PatchMapping(value = "/{id}")
    public AdDto updateAd(@PathVariable Integer id,
                          @RequestBody CreateOrUpdateAdDto crOrUpdAdDto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdDto adDto = adService.updateAd(auth, id, crOrUpdAdDto);
        return adDto;
    }

    @Operation(
            summary = "Получить мои объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Мои объявления получены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Мои объявления не получены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/me")
    public ResponseEntity<AdsDto> getAdsMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(adService.getAllAdsAuth(auth));
    }

    // Метод для изменения картинки объявления
    @Operation(
            summary = "Изменения картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изображение объявления успешно обновлено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка обновления изображения объявления",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    )
            }
    )
    @PreAuthorize("@validationImpl.validateAd(authentication,#id)")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id, @RequestPart("image") MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            byte[] imageData = adService.updateImageAd(auth, id, image);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Получить картинку объявления по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изображение получено по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdImage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Изображение не найдено по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdImage.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageAd(@PathVariable("id") Integer idAd) {
        return ResponseEntity.ok().body(adService.getImageAd(idAd));
    }
}
