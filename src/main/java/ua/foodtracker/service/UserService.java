package ua.foodtracker.service;

import org.springframework.transaction.annotation.Transactional;
import ua.foodtracker.domain.User;

import java.util.Optional;

public interface UserService {

    @Transactional
    void register(User user);

    @Transactional
    void modify(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
