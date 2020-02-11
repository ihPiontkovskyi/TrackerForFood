package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.MealService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ua.foodtracker.utility.ParameterParser.parsePageNumber;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealController {

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
        return "meals";
    }

    @GetMapping(value = "/meals/delete")
    public String deleteMeal(@RequestParam(value = "id", required = false) String id,
                             @SessionAttribute("user") User user,
                             @RequestParam(value = "page", required = false) String page,
                             RedirectAttributes attributes) {
        mealService.delete(id, user);
        attributes.addAttribute("page", page);
        return "redirect:";
    }
}
