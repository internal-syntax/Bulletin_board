package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.*;

import java.io.IOException;
import java.util.*;

public interface AdService {
    AdDto addAd(Authentication auth, CreateOrUpdateAdDto crOrUpdAdDto, MultipartFile image) throws IOException;
    ExtendedAdDto getAd(int adId);
    AdDto updateAd(Authentication auth, int idAd, CreateOrUpdateAdDto crOrUpdAdDto) throws Exception;
    boolean deleteAd(Authentication auth, int idAd);
    AdsDto getAllAds();
    AdsDto getAllAdsAuth(Authentication auth);
    byte[] updateImageAd(Authentication auth, int id, MultipartFile image) throws IOException;
    byte[] getImageAd(int idAd);
}
