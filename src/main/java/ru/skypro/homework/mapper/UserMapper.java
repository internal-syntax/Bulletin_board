package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;


@Mapper
public interface UserMapper {

    @Mapping(target = "image", expression = "java(getImageUri(source))")

    public abstract UserDto convertToDto(User source);

    public abstract User convertToEntity(UpdateUserDto source);

    default String getImageUri(Ad source) {
        if (source.getAdImage() != null) {
            return String.format("/ads/%d/image", source.getId());
        } else {
            return null;
        }
    }
}