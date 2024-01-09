package ru.skypro.homework.controller;

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
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    // Получение всех объявлений
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    // Добавление объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") CreateOrUpdateAdDto createOrUpdateAdDto,
                                       @RequestPart("image") MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdDto adDto = adService.addAd(auth, createOrUpdateAdDto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(adDto);
    }

    // Метод для получения объявления по id
    @GetMapping(value = "/{id}")
    public ExtendedAdDto getAd(@PathVariable Integer id) {
        ExtendedAdDto extendedAdDto = adService.getAd(id);
        return extendedAdDto;
    }

    // Метод для удаления объявления по id
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

    // Изменение объявления
    @PreAuthorize("@validationImpl.validateAd(authentication,#id)")
    @PatchMapping(value = "/{id}")
    public AdDto updateAd(@PathVariable Integer id,
                          @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdDto adDto = adService.updateAd(auth, id, createOrUpdateAdDto);
        return adDto;
    }

    // Получение объявлений авторизованного пользователя
    @GetMapping(value = "/me")
    public ResponseEntity<AdsDto> getAdsMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(adService.getAllAdsAuth(auth));
    }

    // Метод для изменения картинки объявления
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

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageAd(@PathVariable("id") Integer idAd) {
        return ResponseEntity.ok().body(adService.getImageAd(idAd));
    }
}
