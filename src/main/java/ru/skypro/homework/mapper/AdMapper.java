package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.util.UriComponentsBuilder;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression = "java(getImageUri(source))")
    AdsDto convertAdToAdDto(Ad source);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "adImage", expression = "java(getImageUri(source))")
    ExtendedAdDto adToFullAdDto(Ad source);

    AdDto convertAdToAdDto(CreateOrUpdateAdDto createOrUpdateAdDto);

    Ad convertAdDtoToAd(CreateOrUpdateAdDto source);

    void updateAds(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Ad ad);

    default Collection<AdsDto> adsToAdsListDto(Collection<Ad> adsCollection) {
        return adsCollection.stream()
                .map(this::convertAdToAdDto)
                .collect(Collectors.toList());
    }

    default String getImageUri(Ad source) {
        if (source.getAdImage() != null) {
            return String.format("/ads/%d/image", source.getId());
        } else {
            return null;
        }
    }
}
