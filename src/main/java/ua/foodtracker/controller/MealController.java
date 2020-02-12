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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.service.MealService;
import ua.foodtracker.exception.IncorrectDataException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealController {

    public static final String REDIRECT = "redirect:";
    private final MealService mealService;

    @GetMapping(value = "/meals")
    public String mealPage(Model model, @RequestParam(value = "page", required = false) String page) {
        int totalPages = mealService.pageCount();
        model.addAttribute("totalPages", totalPages);
        if (totalPages > 0) {
            List<Long> pageNumbers = LongStream.range(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("page", parsePageNumber(page, 0, totalPages));
        model.addAttribute("meals", mealService.findAllByPage(page));
        return "meals/meals";
    }

    @GetMapping(value = "/meals/delete")
    public String deleteMeal(@RequestParam(value = "id", required = false) String id,
                             @SessionAttribute("user") User user,
                             @RequestParam(value = "page", required = false) String page,
                             RedirectAttributes attributes) {
        mealService.delete(id, user);
        attributes.addAttribute("page", page);
        return REDIRECT;
    }

    @GetMapping(value = "/meals/add")
    public String addMeal(Model model) {
        model.addAttribute("meal", new Meal());
        return "meals/add";
    }

    @PostMapping(value = "/meals/add")
    public String addMeal(@ModelAttribute @Valid Meal meal, @SessionAttribute("user") User user) {
        meal.setUser(user.getRole() == Role.ADMIN ? null : user);
        mealService.add(meal);
        return REDIRECT;
    }

    @GetMapping(value = "/meals/edit")
    public String editMeal(Model model, @RequestParam(required = false) String id, @SessionAttribute("user") User user) {
        Optional<Meal> item = mealService.findById(id);
        if (item.isPresent()) {
            User createdBy = item.get().getUser();
            if (isAdminAccessed(user, createdBy) ||
                    isUserAccessed(user, createdBy)) {
                model.addAttribute("meal", item.get());
            } else {
                throw new AccessDeniedException("access.denied");
            }
        } else {
            throw new IncorrectDataException("incorrect.data");
        }
        return "meals/edit";
    }

    @PostMapping(value = "/meals/edit")
    public String editMeal(@ModelAttribute @Valid Meal meal, @SessionAttribute("user") User user) {
        meal.setUser(user.getRole() == Role.ADMIN ? null : user);
        mealService.modify(meal);
        return REDIRECT;
    }

    private boolean isUserAccessed(User currentUser, User createdBy) {
        return createdBy != null && createdBy.getId().equals(currentUser.getId());
    }

    private boolean isAdminAccessed(User currentUser, User createdBy) {
        return createdBy == null && currentUser.getRole() == Role.ADMIN;
    }
}
