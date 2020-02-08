package ua.foodtracker.service;

import org.springframework.transaction.annotation.Transactional;
import ua.foodtracker.domain.User;

public interface UserService {
    User login(String email, String pass);

    @Transactional
    void register(User user);

    @Transactional
    void modify(User user);

    Object findById(String id);

    void delete(User user);
}
