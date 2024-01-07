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
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.Validation;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public AdDto addAd(Authentication auth, CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile image) throws IOException {
        log.debug("--- выполнение метода сервиса addAd");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем: " + auth.getName() + " не найден"));
        Ad ad = adMapper.convertAdDtoToAd(createOrUpdateAdDto);
        AdImage adImage = new AdImage();
        adImage.setData(image.getBytes());

        ad.setAdImage(adImage);
        ad.setUser(user);
        adRepository.save(ad);
        return adMapper.convertAdToAdDto(createOrUpdateAdDto);
    }

    @Override
    public ExtendedAdDto getAd(int adId) {
        log.debug("--- выполнение метода сервиса getAd");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(AdNotFoundException::new);
        return adMapper.adToFullAdDto(ad);
    }

    @Override
    public AdDto updateAd(Authentication auth, int adId, CreateOrUpdateAdDto createOrUpdateAdDto)
            throws UserNotFoundException, EntityNotFoundException, UnauthorizedUserException {
        log.debug("--- выполнение метода сервиса updateAd");
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено по идентификатору: " + adId));
        if (validation.validateAd(auth, adId)) {
            Ad updatedAd = adMapper.convertAdDtoToAd(createOrUpdateAdDto);
            updatedAd.setId(adId);
            adMapper.updateAds(createOrUpdateAdDto, ad);
            adRepository.save(ad);
            return adMapper.convertAdToAdDto(createOrUpdateAdDto);
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
        Collection<AdsDto> adsDtoList = adMapper.adsToAdsListDto(ads);

        return AdsDto.builder()
                .count(adsDtoList.size())
                .results(adsDtoList)
                .build();
    }

    @Override
    public AdsDto getAllAdsAuth(Authentication auth) throws UserNotFoundException {
        log.debug("--- выполнение метода сервиса getAllAdsAuth");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем: " + auth.getName() + " не найден"));
        List<Ad> adList = adRepository.findAdByUser(user);
        List<AdDto> adDtoList = adList.stream().map(adMapper::convertAdToAdDto).collect(Collectors.toList());

        return AdsDto.builder()
                .count(adDtoList.size())
                .results(adDtoList)
                .build();
    }

    @Override
    public byte[] updateImageAd(Authentication auth, int adId, MultipartFile image)
            throws IOException, UserNotFoundException, EntityNotFoundException {
        log.debug("--- выполнение метода сервиса updateImageAd ");
        User user = userRepository.findUserByLoginIgnoreCase(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем: " + auth.getName() + " не найден"));

        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);
        AdImage adImage = ad.getAdImage();
        adImage.setData(image.getBytes());
        ad.setAdImage(adImage);
        adRepository.save(ad);
        return ad.getAdImage().getData();
    }

    @Override
    public byte[] getImageAd(int adId) throws EntityNotFoundException {
        log.debug("--- выполнение метода сервиса getImageAd");
        return adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено по идентификатору: " + adId))
                .getAdImage().getData();
    }
}
