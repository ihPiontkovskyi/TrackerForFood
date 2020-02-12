package ua.foodtracker.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.UserRepository;
import ua.foodtracker.service.mapper.impl.UserMapper;

import java.util.Optional;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        if (userEntity.isPresent() && encoder.matches(password, userEntity.get().getPassword())) {
            UserEntity entity = userEntity.get();
            return new UsernamePasswordAuthenticationToken(entity.getEmail(), entity.getPassword(), singletonList(new SimpleGrantedAuthority(entity.getRole().name())));
        }
        throw new IncorrectDataException("incorrect.email.or.password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
