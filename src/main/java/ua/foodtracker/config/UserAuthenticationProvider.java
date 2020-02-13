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
import ua.foodtracker.entit.UserEntity;
import ua.foodtracker.exception.LoginFailedException;
import ua.foodtracker.repository.UserRepository;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserEntity userEntity = repository.findByEmail(email).
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
