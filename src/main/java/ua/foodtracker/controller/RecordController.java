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
import ua.foodtracker.service.RecordService;

import java.time.LocalDate;

import static ua.foodtracker.utility.ParameterParser.parseOrDefault;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordController {
    private final RecordService recordService;

    @GetMapping(value = "/home")
    public String home(Model model, @SessionAttribute("user") User user) {
        model.addAttribute("homeModel", recordService.getHomeModel(user));
        return "home";
    }

    @GetMapping(value = "/records/delete")
    public String deleteRecord(@RequestParam("id") String id,
                               @SessionAttribute("user") User user,
                               @RequestParam(value = "date", required = false) String date,
                               RedirectAttributes attributes) {
        recordService.delete(id, user);
        attributes.addAttribute("date", date);
        return "redirect:";
    }

    @GetMapping(value = "/records")
    public String diaryPage(Model model, @SessionAttribute("user") User user,
                            @RequestParam(value = "date", required = false) String date) {
        model.addAttribute("date", parseOrDefault(date, LocalDate.now()));
        model.addAttribute("dailySums", recordService.calculateDailySums(user, date));
        model.addAttribute("records", recordService.getRecordsByDate(user, date));
        return "records";
    }
}
