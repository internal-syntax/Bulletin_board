package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;


@Component
public class UserMapper {

    public UserDto outDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setImage(String.format("/users/avatars/%d", user.getAvatar().getId()));
        return dto;
    }

    public User inDto(UpdateUserDto updateUserDto, User user){
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        String formattedPhoneNumber = formatPhoneNumber(updateUserDto.getPhone());
        user.setPhone(formattedPhoneNumber);
        return user;
    }
    public static String formatPhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = phoneNumber.replaceAll("\\D", "");
        return "+" + formattedPhoneNumber;
    }
}