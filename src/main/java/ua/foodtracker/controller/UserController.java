package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static java.util.Collections.singletonList;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping(value = {"/"})
    public String login() {
        return "login";
    }


    @GetMapping(value = {"/register"})
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = {"/register"})
    public String register(@ModelAttribute @Valid User user) {
        userService.register(user);
        return "login";
    }

}
