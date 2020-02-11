package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.MealService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ua.foodtracker.utility.ParameterParser.parseOrDefault;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MealController {

    private final MealService mealService;

    @GetMapping(value = "/meals")
    public String mealPage(Model model, @RequestParam(value = "page", required = false) String page) {
        long totalPages = mealService.pageCount();
        model.addAttribute("totalPages", totalPages);
        if (totalPages > 0) {
            List<Long> pageNumbers = LongStream.range(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("page", parseOrDefault(page, 0L));
        model.addAttribute("meals", mealService.findAllByPage(page));
        return "meals";
    }

    @GetMapping(value = "/meals/delete")
    public String deleteMeal(@RequestParam("id") String id, @SessionAttribute("user") User user) {
        mealService.delete(id, user);
        return "redirect:meals";
    }
}
