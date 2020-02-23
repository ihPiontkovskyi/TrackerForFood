package ua.foodtracker.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.service.MealService;
import ua.foodtracker.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(MealController.class)
public class MealControllerTest {
    private static final int PAGE_COUNT = 5;
    private static final User USER = getUser();
    private static final Meal MEAL = getMeal();
    private static final Meal MEAL_WITH_USER = getMealWithUser();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private UserService userService;

    @Test
    public void mealPageShouldReturnString() throws Exception {
        when(mealService.findAllByPage(anyString())).thenReturn(Page.empty());
        when(mealService.pageCount()).thenReturn(PAGE_COUNT);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(get("/meals?page=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("totalPages", PAGE_COUNT))
                .andExpect(model().attribute("page", 0))
                .andExpect(model().attribute("meals", Page.empty()
                ))
                .andExpect(model().attribute("user", USER));
    }

    @Test
    public void mealPageShouldThrowUnauthorizedException() throws Exception {
        when(mealService.findAllByPage(anyString())).thenReturn(Page.empty());
        when(mealService.pageCount()).thenReturn(PAGE_COUNT);
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mvc.perform(get("/meals?page=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser
    public void deleteMealShouldReturnString() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(get("/meals/delete?id=1&page=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"))
                .andExpect(model().attribute("page", "1"));
    }

    @Test
    @WithMockUser
    public void addMealShouldReturnString() throws Exception {
        mvc.perform(get("/meals/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-meal"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void addMealShouldReturnStringCase2() throws Exception {
        mvc.perform(post("/meals/add")
                .param("name", "name")
                .param("weight", "100")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void addMealShouldReturnStringCase3() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/meals/add")
                .param("name", "name")
                .param("weight", "100")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"));
    }

    @Test
    @WithMockUser
    public void addMealShouldThrowUnauthorizedException() throws Exception {
        mvc.perform(post("/meals/add")
                .param("name", "name")
                .param("weight", "100")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser
    public void addMealShouldNotValidate() throws Exception {
        mvc.perform(post("/meals/add")
                .param("name", "name")
                .param("weight", "name")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void editMealPageShouldThrowUnauthorizedException() throws Exception {
        when(mealService.findById("1")).thenReturn(MEAL);

        mvc.perform(get("/meals/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void editMealPageShouldStatusOk() throws Exception {
        when(mealService.findById("1")).thenReturn(MEAL_WITH_USER);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(get("/meals/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("meal", MEAL_WITH_USER))
                .andExpect(view().name("edit-meal"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void editMealPageShouldThrowAccessDeniedExc() throws Exception {
        when(mealService.findById("1")).thenReturn(MEAL_WITH_USER);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(getUserWithId2()));

        mvc.perform(get("/meals/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void editMealPageShouldReturnString() throws Exception {
        when(mealService.findById("1")).thenReturn(MEAL);

        mvc.perform(get("/meals/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("meal", MEAL))
                .andExpect(view().name("edit-meal"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void editMealShouldReturnString() throws Exception {
        mvc.perform(post("/meals/edit")
                .param("name", "name")
                .param("weight", "10")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void editMealShouldReturnStringCase2() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/meals/edit")
                .param("name", "name")
                .param("weight", "10")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"));
    }

    @Test
    @WithMockUser
    public void editMealShouldNotValidate() throws Exception {
        mvc.perform(post("/meals/edit")
                .param("name", "name")
                .param("weight", "name")
                .param("protein", "10")
                .param("fat", "10")
                .param("carbohydrate", "10")
                .param("water", "10")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    public void mealsAutocompleteShouldReturnEmptyJsonString() throws Exception {
        when(mealService.findAllByNameStartWith("term")).thenReturn(Collections.emptyList());

        mvc.perform(get("/by-term?term=term"))
                .andExpect(content().string("[]"));
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

    private static User getUserWithId2() {
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
        user.setId(2);
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
