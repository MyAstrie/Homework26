import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

public class UserRepositoryTest {
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void testGetAllUsers_emptyList() {
        List<User> users = userRepository.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetAllUsers_filledList() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        List<User> actualUsers = userRepository.getUsers();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testFindUserByLogin_exists() {
        User user = new User("user", "password");
        userRepository.addUser(user);
        Optional<User> foundUser = userRepository.findByLogin("user");
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void testFindUserByLogin_notExists() {
        Optional<User> foundUser = userRepository.findByLogin("user");
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testFindUserByLoginAndPassword_exists() {
        User user = new User("user", "password");
        userRepository.addUser(user);
        Optional<User> foundUser = userRepository.findByLoginAndPassword("user", "password");
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void testFindUserByLoginAndPassword_wrongPassword() {
        User user = new User("user", "password");
        userRepository.addUser(user);
        Optional<User> foundUser = userRepository.findByLoginAndPassword("user", "wrongPassword");
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testFindUserByLoginAndPassword_wrongUsername() {
        User user = new User("user", "password");
        userRepository.addUser(user);
        Optional<User> foundUser = userRepository.findByLoginAndPassword("wrongUsername", "password");
        assertFalse(foundUser.isPresent());
    }
}
