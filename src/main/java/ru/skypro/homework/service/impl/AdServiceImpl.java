package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.EntityNotFoundException;
import ru.skypro.homework.exceptions.UnauthorizedUserException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.*;
import ru.skypro.homework.repositories.AdImageRepository;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.Validation;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static liquibase.repackaged.net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdImageRepository adImageRepository;
    private final Validation validation;
    private final ImageService imageService;

    @Override
    public AdDto addAd(Authentication auth, CreateOrUpdateAdDto crOrUpdAdDto, MultipartFile image) throws IOException {
        log.debug("--- выполнение метода сервиса addAd");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем: " + auth.getName() + " не найден"));
        String imagePath = imageService.createImagePath(image);
        imageService.upload(imagePath, image);
        Ad ad = adMapper.inDtoUpdate(crOrUpdAdDto);
        AdImage adImage = new AdImage();
        adImage.setImagePath(imagePath);

        imageService.save(adImage);
        ad.setAdImage(adImage);
        ad.setUser(user);
        adRepository.save(ad);
        return adMapper.outDtoAd(ad);
    }

    @Override
    public ExtendedAdDto getAd(int adId) {
        log.debug("--- выполнение метода сервиса getAd");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(AdNotFoundException::new);
        return adMapper.outDExtendedAdDto(ad);
    }

    @Override
    public AdDto updateAd(Authentication auth, int adId, CreateOrUpdateAdDto crOrUpdAdDto)
            throws UserNotFoundException, EntityNotFoundException, UnauthorizedUserException {
        log.debug("--- выполнение метода сервиса updateAd");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));
        if (validation.validateAd(auth, adId)) {
            Ad updatedAd = adMapper.inDtoUpdate(crOrUpdAdDto, ad);
            adRepository.save(updatedAd);
            return adMapper.outDtoAd(updatedAd);
        } else {
            throw new UnauthorizedUserException("Пользователь не авторизован для изменения объявления");
        }
    }

    @Override
    public boolean deleteAd(Authentication auth, int idAd) throws UserNotFoundException {
        log.debug("--- выполнение метода сервиса deleteAd");
        if (adRepository.findById(idAd).isPresent()) {
            adRepository.deleteById(idAd);
            return true;
        }
        return false;
    }

    @Override
    public AdsDto getAllAds() {
        log.debug("--- выполнение метода сервиса getAllAds");
        List<Ad> ads = adRepository.findAll();
        List<AdDto> adDtoList = ads.stream()
                .map(adMapper::outDtoAd)
                .collect(Collectors.toList());
        return adMapper.outDtoAll(adDtoList);
    }

    @Override
    public AdsDto getAllAdsAuth(Authentication auth) throws UserNotFoundException {
        log.debug("--- выполнение метода сервиса getAllAdsAuth");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + auth.getName()));
        List<Ad> adList = adRepository.findAdByUser(user);
        List<AdDto> adDtoList = adList.stream().map(adMapper::outDtoAd).collect(Collectors.toList());
        return adMapper.outDtoAll(adDtoList);
    }

    @Override
    public byte[] updateImageAd(Authentication auth, int adId, MultipartFile image)
            throws IOException, UserNotFoundException, EntityNotFoundException {
        log.debug("--- выполнение метода сервиса updateImageAd ");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем: " + auth.getName() + " не найден"));

        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);
        AdImage adImage = ad.getAdImage();
        /*        adImage.setData(image.getBytes());*/
        ad.setAdImage(adImage);
        adRepository.save(ad);
        return imageService.download(ad.getAdImage().getImagePath());
        /*        return ad.getAdImage().getData();*/
    }

    @Override
    public byte[] getImageAd(int adId) throws EntityNotFoundException {
        log.debug("--- выполнение метода сервиса getImageAd");
        AdImage adImage = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено по идентификатору: " + adId))
                .getAdImage();

        return imageService.download(adImage.getImagePath());
    }
}
