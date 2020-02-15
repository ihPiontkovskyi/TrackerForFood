package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.User;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.exception.LoginFailedException;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.UserService;
import ua.foodtracker.service.mapper.Mapper;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    public static final String INCORRECT_DATA = "incorrect.data";
    private final UserRepository userRepository;
    private final Mapper<User, UserEntity> userMapper;
    private final PasswordEncoder encoder;

    @Override
    public void register(User user) {
        if (nonNull(user.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
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
        if (isNull(user.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        userRepository.save(userMapper.mapToEntity(user));
    }

    @Override
    public Optional<User> findById(String id) {
        return findByStringParam(id, userRepository::findById)
                .map(userMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (isNull(email)) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        return userRepository.findByEmail(email)
                .map(userMapper::mapToDomain);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(() -> new LoginFailedException("user.does.not.exists"));

        if (encoder.matches(password, userEntity.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), singletonList(new SimpleGrantedAuthority(userEntity.getRole().name())));
        }

        throw new LoginFailedException("incorrect.email.or.password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
