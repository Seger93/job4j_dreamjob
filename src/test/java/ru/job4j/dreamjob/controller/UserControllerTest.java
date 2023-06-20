package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPage() {
        var view = userController.getRegistationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUser() {
        var user = new User(1, "serg@mail.ru", "Ivan", "111");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(user).isEqualTo(actualUser);
    }

    @Test
    public void whenGetLoginPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserIsFalse() {
        var request = mock(HttpServletRequest.class);
        var user = new User(1, "serg@mail.ru", "Ivan", "111");
        var user1 = new User(2, "test@mail.ru", "test", "1111");
        userService.save(user);
        userService.save(user1);
        when(userService.findByEmailAndPassword(user1.getEmail(), "2222")).thenReturn(Optional.empty());
        when(request.getSession()).thenReturn(new MockHttpSession());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user1, model, request);

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserIsTrue() {
        var request = mock(HttpServletRequest.class);
        var user = new User(1, "serg@mail.ru", "Ivan", "111");
        var user1 = new User(2, "test@mail.ru", "test", "1111");
        userService.save(user);
        when(userService.findByEmailAndPassword(user1.getEmail(), user1.getPassword())).thenReturn(Optional.of(user1));
        when(request.getSession()).thenReturn(new MockHttpSession());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user1, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserLogout() {
        var request = new MockHttpSession();
        var view = userController.logout(request);

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}