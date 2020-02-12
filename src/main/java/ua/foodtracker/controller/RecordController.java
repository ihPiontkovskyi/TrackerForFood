package ua.foodtracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.foodtracker.domain.User;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.UserService;

import java.time.LocalDate;

import static ua.foodtracker.controller.ControllerHelper.getUserFromSecurityContext;
import static ua.foodtracker.utility.ParameterParser.parseOrDefault;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordController {
    private final RecordService recordService;
    private final UserService userService;

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Model model) {
        model.addAttribute("homeModel", recordService.getHomeModel(getUserFromSecurityContext(userService)));
        return "home";
    }

    @GetMapping(value = "/records/delete")
    public String deleteRecord(@RequestParam("id") String id,
                               @RequestParam(value = "date", required = false) String date,
                               RedirectAttributes attributes) {
        recordService.delete(id, getUserFromSecurityContext(userService));
        attributes.addAttribute("date", date);
        return "redirect:";
    }

    @GetMapping(value = "/records")
    public String diaryPage(Model model,
                            @RequestParam(value = "date", required = false) String date) {
        User user = getUserFromSecurityContext(userService);
        model.addAttribute("date", parseOrDefault(date, LocalDate.now()));
        model.addAttribute("dailySums", recordService.calculateDailySums(user, date));
        model.addAttribute("records", recordService.getRecordsByDate(user, date));
        return "records/records";
    }
}
