package ua.foodtracker.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.transaction.annotation.Transactional;
import ua.foodtracker.domain.User;

import java.util.Optional;

public interface UserService extends AuthenticationProvider {

    @Transactional
    void register(User user);

    @Transactional
    void modify(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
