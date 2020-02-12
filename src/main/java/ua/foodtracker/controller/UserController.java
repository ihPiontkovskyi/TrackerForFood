package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final DaoAuthenticationProvider provider;

    @GetMapping(value = {"/"})
    public String login() {
        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(@RequestParam("email") String email,
                        @RequestParam("pass") String password,
                        HttpSession session) {
        User user = userService.login(email, password);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email,password);
        provider.authenticate(authentication);
        session.setAttribute("user", user);
        return "redirect:/home";
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
