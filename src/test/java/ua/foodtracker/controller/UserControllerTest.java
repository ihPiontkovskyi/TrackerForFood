package ua.foodtracker.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.foodtracker.domain.Gender;
import ua.foodtracker.domain.Lifestyle;
import ua.foodtracker.domain.Role;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.UserGoal;
import ua.foodtracker.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final User USER = getUser();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void loginShouldReturnString() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void registerShouldReturnString() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void registerShouldPassSuccessfully() throws Exception {

        mvc.perform(post("/register")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("repeatPassword", "password")
                .param("weight", "80")
                .param("height", "190")
                .param("lifestyle", "SEDENTARY")
                .param("first_name", "Name")
                .param("last_name", "Name")
                .param("birthday", "2020-01-01")
                .param("gender", "FEMALE")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void registerShouldNotPassValidation() throws Exception {

        mvc.perform(post("/register")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("repeatPassword", "password")
                .param("weight", "weight")
                .param("height", "190")
                .param("lifestyle", "SEDENTARY")
                .param("first_name", "Name")
                .param("last_name", "Name")
                .param("birthday", "2020-01-01")
                .param("gender", "FEMALE")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
    }

    @Test
    @WithMockUser
    public void profileShouldReturnString() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser
    public void profileShouldChangeUser() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/profile")
                .param("weight", "80")
                .param("height", "190")
                .param("lifestyle", "SEDENTARY")
                .param("first_name", "Name")
                .param("last_name", "Name")
                .param("birthday", "2020-01-01")
                .param("gender", "FEMALE")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser
    public void profileShouldNotPassValidation() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/profile")
                .param("weight", "80")
                .param("height", "190")
                .param("lifestyle", "SEDENTARY")
                .param("first_name", "Name")
                .param("last_name", "Name")
                .param("birthday", "01-01")
                .param("gender", "FEMALE")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error-page"));
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
}
