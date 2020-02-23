package ua.foodtracker.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.foodtracker.domain.DailySums;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.HomeModel;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.service.DateProvider;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(RecordController.class)
public class RecordControllerTest {
    private static final User USER = getUser();
    private static final Meal MEAL = getMeal();
    private static final Meal MEAL_WITH_USER = getMealWithUser();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecordService recordService;
    @MockBean
    private DateProvider dateProvider;
    @MockBean
    private UserService userService;
    @MockBean
    private MealService mealService;

    @Test
    @WithMockUser
    public void getHomeShouldStatusOk() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        final DailySums dailySums = DailySums.builder()
                .sumWater(20)
                .sumProtein(19)
                .sumFat(19)
                .sumEnergy(1200)
                .sumCarbohydrates(129)
                .build();
        final HomeModel homeModel = HomeModel.builder()
                .labels(Collections.emptyList())
                .weeklyStats(new HashMap<>())
                .dailySums(dailySums)
                .dailyGoal(HomeModel.calculateDailyGoal(dailySums, USER.getUserGoal()))
                .build();
        when(recordService.getHomeModel(USER)).thenReturn(homeModel);

        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("homeModel", homeModel))
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser
    public void postHomePageShouldStatusOk() throws Exception {

        mvc.perform(post("/home-page"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser
    public void getHomeShouldUnauthorized() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser
    public void deleteRecordShouldReturnString() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(get("/records/delete?id=1&date=2020-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"))
                .andExpect(model().attribute("date", "2020-01-01"));
    }

    @Test
    @WithMockUser
    public void addRecordShouldReturnString() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        when(dateProvider.parseOrCurrentDate("2020-01-01")).thenReturn(LocalDate.of(2020, 1, 1));

        mvc.perform(post("/records/add")
                .param("meal_id", "1")
                .param("date", "2020-01-01")
                .param("weight", "320"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"))
                .andExpect(model().attribute("date", "2020-01-01"));
    }

    @Test
    @WithMockUser
    public void recordsPageShouldReturnString() throws Exception {
        final DailySums dailySums = DailySums.builder()
                .sumWater(20)
                .sumProtein(19)
                .sumFat(19)
                .sumEnergy(1200)
                .sumCarbohydrates(129)
                .build();
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        when(recordService.getRecordsByDate(USER, "2020-01-01")).thenReturn(Collections.emptyList());
        when(dateProvider.parseOrCurrentDate("2020-01-01")).thenReturn(LocalDate.of(2020, 1, 1));
        when(recordService.calculateDailySums(USER, "2020-01-01")).thenReturn(dailySums);

        mvc.perform(get("/records?date=2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("records"))
                .andExpect(model().attribute("dailySums", dailySums))
                .andExpect(model().attribute("records", Collections.emptyList()));
    }

    @Test
    @WithMockUser
    public void recordsPageShouldReturnStringCase2() throws Exception {
        final DailySums dailySums = DailySums.builder()
                .sumWater(20)
                .sumProtein(19)
                .sumFat(19)
                .sumEnergy(2500)
                .sumCarbohydrates(129)
                .build();
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        when(recordService.getRecordsByDate(USER, "2020-01-01")).thenReturn(Collections.emptyList());
        when(dateProvider.parseOrCurrentDate("2020-01-01")).thenReturn(LocalDate.of(2020, 1, 1));
        when(recordService.calculateDailySums(USER, "2020-01-01")).thenReturn(dailySums);

        mvc.perform(get("/records?date=2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("records"))
                .andExpect(model().attribute("dailySums", dailySums))
                .andExpect(model().attribute("dailySums", dailySums))
                .andExpect(model().attribute("exceedingTheGoal", true))
                .andExpect(model().attribute("exceedingValue", 400))
                .andExpect(model().attribute("records", Collections.emptyList()));
    }

    private static User getUser() {
        User user = new User();
        user.setBirthday(LocalDate.now().minusYears(20));
        user.setFirstName("first-name");
        user.setEmail("email");
        user.setGender(Gender.MALE);
        user.setHeight(80);
        user.setLastName("last-name");
        user.setLifestyle(Lifestyle.SEDENTARY);
        user.setPassword("hash");
        user.setRepeatPassword("hash");
        user.setWeight(80);
        user.setId(1);
        user.setRole(Role.USER);
        user.setUserGoal(getUserGoal());
        return user;
    }

    private static UserGoal getUserGoal() {
        return UserGoal.builder()
                .dailyWaterGoal(2000)
                .dailyProteinGoal(100)
                .dailyFatGoal(100)
                .dailyEnergyGoal(2100)
                .dailyCarbohydrateGoal(100)
                .id(1)
                .build();
    }

    private static Meal getMeal() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(10);
        meal.setUser(null);
        return meal;
    }

    private static Meal getMealWithUser() {
        Meal meal = new Meal();
        meal.setCarbohydrate(10);
        meal.setFat(10);
        meal.setId(1);
        meal.setName("name");
        meal.setProtein(10);
        meal.setWater(10);
        meal.setWeight(10);
        meal.setUser(USER);
        return meal;
    }
}
