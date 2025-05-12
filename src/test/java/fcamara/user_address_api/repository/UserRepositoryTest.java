package fcamara.user_address_api.repository;

import fcamara.user_address_api.entity.Role;
import fcamara.user_address_api.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save a User and retrieve it by email")
    void shouldSaveAndRetrieveUserByEmail() {
        User user = User.builder()
                .email("john.doe@example.com")
                .name("John Doe")
                .password("Test")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("john.doe@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return empty Optional when email not found")
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");
        assertThat(result).isEmpty();
    }
}