package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.MealInfo;
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ua.foodtracker.controller.ControllerHelper.getUserAuthorities;
import static ua.foodtracker.controller.ControllerHelper.getUserFromSecurityContext;
import static ua.foodtracker.controller.ControllerHelper.isAdminAccessed;
import static ua.foodtracker.controller.ControllerHelper.isUserAccessed;
import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealController {

    public static final String REDIRECT = "redirect:";
    private final MealService mealService;
    private final UserService userService;

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
        model.addAttribute("user", getUserFromSecurityContext(userService));
        return "meals";
    }

    @GetMapping(value = "/meals/delete")
    public String deleteMeal(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "page", required = false) String page,
                             RedirectAttributes attributes) {
        mealService.delete(id, getUserFromSecurityContext(userService));
        attributes.addAttribute("page", page);
        return REDIRECT;
    }

    @GetMapping(value = "/meals/add")
    public String addMeal(Model model) {
        model.addAttribute("meal", new Meal());
        return "add-meal";
    }

    @PostMapping(value = "/meals/add")
    public String addMeal(@ModelAttribute @Valid Meal meal) {
        meal.setUser(getUserAuthorities().contains("ADMIN") ? null : getUserFromSecurityContext(userService));
        mealService.add(meal);
        return REDIRECT;
    }

    @GetMapping(value = "/meals/edit")
    public String editMeal(Model model, @RequestParam(required = false) String id) {
        Meal item = mealService.findById(id);

        if (isAdminAccessed(item) ||
                isUserAccessed(item, getUserFromSecurityContext(userService))) {
            model.addAttribute("meal", item);
        } else {
            throw new AccessDeniedException("access.denied");
        }
        return "edit-meal";
    }

    @PostMapping(value = "/meals/edit")
    public String editMeal(@ModelAttribute @Valid Meal meal) {
        meal.setUser(getUserAuthorities().contains("ADMIN") ? null : getUserFromSecurityContext(userService));
        mealService.modify(meal);
        return REDIRECT;
    }

    @GetMapping(value = {"/by-term", "/records/by-term"})
    @ResponseBody
    public List<MealInfo> mealsAutocomplete(@RequestParam(value = "term", required = false, defaultValue = "") String term) {
        return mealService.findAllByNameStartWith(term).stream()
                .limit(10)
                .map(ControllerHelper::getMealInfo)
                .collect(Collectors.toList());
    }
}
