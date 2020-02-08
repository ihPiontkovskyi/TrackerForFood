package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.UserService;
import ua.foodtracker.service.exception.IncorrectDataException;
import ua.foodtracker.service.utility.Mapper;

import java.util.Optional;

import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.Mapper.mapUserDomainToUserEntity;
import static ua.foodtracker.service.utility.Mapper.mapUserEntityToUserDomain;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User login(String email, String pass) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent() && checkpw(userEntity.get().getPassword(), pass)) {
            return mapUserEntityToUserDomain(userEntity.get());
        }
        throw new IncorrectDataException("incorrect.email.or.password");
    }

    @Override
    public void register(User user) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(user.getEmail());
        if (!userEntity.isPresent()) {
            userRepository.save(mapUserDomainToUserEntity(user));
        }
        throw new IncorrectDataException("user.with.email.already.exists");
    }

    @Override
    public void modify(User user) {
        userRepository.save(mapUserDomainToUserEntity(user));
    }

    @Override
    public Optional<User> findById(String id) {
        return findByStringParam(id, userRepository::findById).map(Mapper::mapUserEntityToUserDomain);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(mapUserDomainToUserEntity(user));
    }
}
