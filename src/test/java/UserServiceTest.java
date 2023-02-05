import org.example.exceptions.UserNonUniqueException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testGetAllUserLogins() {
        List<User> users = new ArrayList<>();
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        users.add(user1);
        users.add(user2);

        when(userRepository.getUsers()).thenReturn(users);

        List<String> expectedLogins = new ArrayList<>();
        expectedLogins.add("user1");
        expectedLogins.add("user2");

        List<String> actualLogins = userService.getAllUserLogins();

        assertEquals(expectedLogins, actualLogins);
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    public void testCreateNewUser() throws UserNonUniqueException {
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());
        doNothing().when(userRepository).addUser(any(User.class));

        userService.createNewUser("user1", "password1");

        verify(userRepository, times(1)).findByLogin("user1");
        verify(userRepository, times(1)).addUser(any(User.class));
    }

    @Test(expected = UserNonUniqueException.class)
    public void testCreateNewUser_UserNonUniqueException() throws UserNonUniqueException {
        User user = new User("user1", "password1");
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(user));

        userService.createNewUser("user1", "password1");

        verify(userRepository, times(1)).findByLogin("user1");
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNewUser_IllegalArgumentException() throws UserNonUniqueException {
        userService.createNewUser(null, null);

        verify(userRepository, never()).findByLogin(any(String.class));
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    public void testCheckUserLogin_SuccessfulLogin() {
        User user = new User("user1", "password1");
        when(userRepository.findByLoginAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));

        boolean result = userService.checkUserLogin("user1", "password1");

        assertTrue(result);
        verify(userRepository, times(1)).findByLoginAndPassword("user1", "password1");
    }

    @Test
    public void testCheckUserLogin_UnsuccessfulLogin() {
        when(userRepository.findByLoginAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        boolean result = userService.checkUserLogin("user1", "password1");

        assertFalse(result);
        verify(userRepository, times(1)).findByLoginAndPassword("user1", "password1");
    }
}
