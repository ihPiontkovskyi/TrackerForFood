package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.UserService;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping(value = {"/"})
    public String login() {
        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(Model model, @RequestParam("email") String email, @RequestParam("pass") String password) {
        User user = userService.login(email, password);
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping(value = {"/register"})
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = {"/register"})
    public String register(@ModelAttribute User user) {
        userService.register(user);
        return "login";
    }
}