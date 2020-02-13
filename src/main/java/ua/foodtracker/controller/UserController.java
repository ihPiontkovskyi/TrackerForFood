package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserProfileInfo;
import ua.foodtracker.service.UserService;

import javax.validation.Valid;

import static ua.foodtracker.controller.ControllerHelper.getUserProfile;
import static ua.foodtracker.controller.ControllerHelper.makeChanges;

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

    @GetMapping(value = {"/profile"})
    public String userProfile(Model model) {
        model.addAttribute("user", getUserProfile(userService));
        return "user/profile";
    }

    @PostMapping(value = {"/profile"})
    public String userProfile(@ModelAttribute @Valid UserProfileInfo userProfile) {
        User user = makeChanges(userProfile, userService);
        userService.modify(user);
        return "redirect:/home";
    }
}
