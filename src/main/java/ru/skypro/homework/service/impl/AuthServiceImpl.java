package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AuthService;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserServiceImpl detailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public boolean login(String userName,String password) {
        log.debug("--- service started login");
        if (!userRepository.existsByLoginIgnoreCase(userName)) {
            return false;
        }
        UserDetails userDetails = detailsService.loadUserByUsername(userName);
        if (encoder.matches(password, userDetails.getPassword())) {
            log.debug("--- user login: " + userName);
            return true;
        }
        return false;
    }

    @Override
    public boolean register(Register register) {
        log.debug("--- service started register");
        if (userRepository.existsByLoginIgnoreCase(register.getUsername())) {
            return false;
        }

        User newUser = createUser(register);

        try {
            userRepository.save(newUser);
            log.debug("--- user registered: " + register.getUsername());
            return true;
        }catch (Exception e){
            log.error("error during user registration: " + e.getMessage());
            return false;
        }
    }

    private User createUser(Register register) {
        User newUser = new User();
        newUser.setLogin(register.getUsername());
        newUser.setPassword(encoder.encode(register.getPassword()));
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());
        newUser.setAvatar(new Avatar());
        return newUser;
    }
}
