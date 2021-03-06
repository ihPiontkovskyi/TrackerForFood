package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foodtracker.domain.DailySums;
import ua.foodtracker.domain.Record;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.DateProvider;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.UserService;

import static ua.foodtracker.controller.ControllerHelper.getUserFromSecurityContext;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordController {
    private final RecordService recordService;
    private final UserService userService;
    private final MealService mealService;
    private final DateProvider dateProvider;

    @GetMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("homeModel", recordService.getHomeModel(getUserFromSecurityContext(userService)));
        return "home";
    }

    @PostMapping(value = "/home-page")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping(value = "/records/delete")
    public String deleteRecord(@RequestParam("id") String id,
                               @RequestParam(value = "date", required = false) String date,
                               RedirectAttributes attributes) {
        recordService.delete(id, getUserFromSecurityContext(userService));
        attributes.addAttribute("date", date);
        return "redirect:";
    }

    @PostMapping(value = "/records/add")
    public String addRecord(@RequestParam("meal_id") String id,
                            @RequestParam("weight") Integer weight,
                            @RequestParam(value = "date", required = false) String date,
                            RedirectAttributes attributes) {
        Record record = new Record();
        record.setUser(getUserFromSecurityContext(userService));
        record.setMeal(mealService.findById(id));
        record.setDate(dateProvider.parseOrCurrentDate(date));
        record.setWeight(weight);
        recordService.add(record);
        attributes.addAttribute("date", date);
        return "redirect:";
    }

    @GetMapping(value = "/records")
    public String diaryPage(Model model,
                            @RequestParam(value = "date", required = false) String date) {
        User user = getUserFromSecurityContext(userService);
        model.addAttribute("date", dateProvider.parseOrCurrentDate(date));
        final DailySums dailySums = recordService.calculateDailySums(user, date);
        if (dailySums.getSumEnergy() > user.getUserGoal().getDailyEnergyGoal()) {
            model.addAttribute("exceedingTheGoal", true);
            model.addAttribute("exceedingValue", dailySums.getSumEnergy() - user.getUserGoal().getDailyEnergyGoal());
        }
        model.addAttribute("dailySums", dailySums);
        model.addAttribute("records", recordService.getRecordsByDate(user, date));
        return "records";
    }
}
