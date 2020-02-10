package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppController {

    private final UserService userService;
    private final RecordService recordService;
    private final MealService mealService;

    @GetMapping(value = {"/"})
    public String login() {
        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(HttpSession session, @RequestParam("email") String email, @RequestParam("pass") String password) {
        User user = userService.login(email, password);
        session.setAttribute("user", user);
        return "redirect:/home";
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

    @GetMapping(value = "/home")
    public String home(Model model, @SessionAttribute("user") User user) {
        model.addAttribute("homeModel", recordService.prepareHomeModel(user));
        return "home";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping(value = "/meals")
    public String mealPage(Model model, @RequestParam("page") Optional<String> page) {
        long totalPages = mealService.pageCount();
        model.addAttribute("totalPages", totalPages);
        if (totalPages > 0) {
            List<Long> pageNumbers = LongStream.range(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("page", parseOrDefault(page, 0L));
        model.addAttribute("meals", mealService.findAllByPage(page.orElse("1")));
        return "meals";
    }

    @GetMapping(value = "/records")
    public String diaryPage(Model model, @SessionAttribute("user") User user, @RequestParam("date") Optional<String> date) {
        model.addAttribute("date", parseOrDefault(date, LocalDate.now()));
        model.addAttribute("dailySums", recordService.calculateDailySums(user, date.orElseGet(() -> LocalDate.now().toString())));
        model.addAttribute("records", recordService.getRecordsByDate(user, date.orElseGet(() -> LocalDate.now().toString())));
        return "records";
    }

    private LocalDate parseOrDefault(Optional<String> param, LocalDate defaultValue) {
        if (!param.isPresent()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(param.get());
        } catch (DateTimeParseException ex) {
            return defaultValue;
        }
    }

    private Long parseOrDefault(Optional<String> param, Long defaultValue) {
        if (!param.isPresent()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(param.get());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
