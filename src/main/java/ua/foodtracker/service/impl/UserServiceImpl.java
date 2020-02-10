package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.UserService;
import ua.foodtracker.service.exception.IncorrectDataException;
import ua.foodtracker.service.mapper.impl.UserMapper;

import java.util.Optional;

import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public User login(String email, String pass) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent() && checkpw(pass, userEntity.get().getPassword())) {
            return userMapper.mapToDomain(userEntity.get());
        }
        throw new IncorrectDataException("incorrect.email.or.password");
    }

    @Override
    public void register(User user) {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            throw new IncorrectDataException("passwords.do.not.match");
        }
        if (!userRepository.existsByEmail(user.getEmail())) {
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
    public void delete(User user) {
        userRepository.delete(userMapper.mapToEntity(user));
    }
}
