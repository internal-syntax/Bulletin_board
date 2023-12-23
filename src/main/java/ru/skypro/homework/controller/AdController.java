package ru.skypro.homework.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.*;

@RestController
@RequestMapping("/api/ads")
public class AdController {

//    private final AdServiceImpl adService;

//    public AdController(AdServiceImpl adService) {
//        this.adService = adService;
//    }

    // Метод для получения всех объявлений
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
//        AdsDto adsDto = adService.getAllAds();
//        return ResponseEntity.ok(adsDto);
        return ResponseEntity.ok(new AdsDto());
    }

    // Метод для добавления объявления
    @PostMapping
    public ResponseEntity<AdDto> addAd(
            @RequestPart("image") MultipartFile image,
            @RequestPart("properties") CreateOrUpdateAdDto createOrUpdateAdDto) {
//        AdDto newAdDto = adService.addAd(image, createOrUpdateAdDto);
//        return ResponseEntity.status(201).body(newAdDto);
        return ResponseEntity.ok(new AdDto());
    }

    // Метод для получения комментариев к объявлению
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable int id) {
//        CommentsDto commentsDto = adService.getComments(id);
//        return ResponseEntity.ok(commentsDto);
        return ResponseEntity.ok(new CommentsDto());
    }

    // Метод для добавления комментария к объявлению
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int id,
            @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
//        CommentDto newCommentDto = adService.addComment(id, createOrUpdateCommentDto);
//        return ResponseEntity.ok(newCommentDto);
        return ResponseEntity.ok(new CommentDto());
    }
}
