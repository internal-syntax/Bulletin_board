package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

import java.util.List;


@Component
public class AdMapper {

    public ExtendedAdDto outDExtendedAdDto(Ad ad) {
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();

        extendedAdDto.setPk(ad.getId());
        extendedAdDto.setAuthorFirstName(ad.getUser().getFirstName());
        extendedAdDto.setAuthorLastName(ad.getUser().getLastName());
        extendedAdDto.setDescription(ad.getDescription());
        extendedAdDto.setEmail(ad.getUser().getLogin());
        extendedAdDto.setImage(String.format("/ads/%d/image",ad.getAdImage().getId()));
        extendedAdDto.setPhone(ad.getUser().getPhone());
        extendedAdDto.setPrice(ad.getPrice());
        extendedAdDto.setTitle(ad.getTitle());

        return extendedAdDto;
    }

    public AdsDto outDtoAll(List<AdDto> adListDto) {
        AdsDto adsAll = new AdsDto();

        adsAll.setCount(adListDto.size());
        adsAll.setResults(adListDto);

        return adsAll;
    }

    public AdDto outDtoAd(Ad ad) {
        AdDto adDto = new AdDto();
        adDto.setAuthor(ad.getUser().getId());
        adDto.setImage(String.format("/ads/%d/image",ad.getAdImage().getId()));
        adDto.setPk(ad.getId());
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        return adDto;
    }

    public Ad inDtoUpdate(CreateOrUpdateAdDto crOrUpdAdDto) {
        Ad ad = new Ad();
        ad.setTitle(crOrUpdAdDto.getTitle());
        ad.setPrice(crOrUpdAdDto.getPrice());
        ad.setDescription(crOrUpdAdDto.getDescription());
        return ad;
    }
    public Ad inDtoUpdate(CreateOrUpdateAdDto crOrUpdAdDto, Ad ad) {
        ad.setTitle(crOrUpdAdDto.getTitle());
        ad.setPrice(crOrUpdAdDto.getPrice());
        ad.setDescription(crOrUpdAdDto.getDescription());
        return ad;
    }

/*    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression = "java(getImageUri(source))")
    AdDto convertAdToAdDto(Ad source);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "adImage", expression = "java(getImageUri(source))")
    ExtendedAdDto adToFullAdDto(Ad source);

    AdDto convertAdToAdDto(CreateOrUpdateAdDto createOrUpdateAdDto);

    Ad convertAdDtoToAd(CreateOrUpdateAdDto source);

    void updateAds(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Ad ad);

    default String getImageUri(Ad source) {
        if (source.getAdImage() != null) {
            return String.format("/ads/%d/image", source.getId());
        } else {
            return null;
        }
    }*/
}
