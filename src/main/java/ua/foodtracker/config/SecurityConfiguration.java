package ua.foodtracker.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationProvider provider;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/static/**", "/login", "/register").permitAll()
                .antMatchers("/home", "/meals/**", "/records/**").authenticated()

                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("pass")
                .successForwardUrl("/home").permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .permitAll()

                .and()
                .csrf()
                .disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }
}
