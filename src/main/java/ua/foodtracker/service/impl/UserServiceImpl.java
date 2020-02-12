package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.UserService;
import ua.foodtracker.service.mapper.impl.UserMapper;

import java.util.Optional;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public void register(User user) {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            throw new IncorrectDataException("passwords.do.not.match");
        }
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(userMapper.mapToEntity(user));
            return;
        }
        throw new IncorrectDataException("user.with.email.already.exists");
    }

    @Override
    public void modify(User user) {
        userRepository.save(userMapper.mapToEntity(user));
    }

    @Override
    public Optional<User> findById(String id) {
        return findByStringParam(id, userRepository::findById).map(userMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::mapToDomain);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(userMapper.mapToEntity(user));
    }
}
