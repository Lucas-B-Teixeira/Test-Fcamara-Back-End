package fcamara.user_address_api.repository;

import fcamara.user_address_api.entity.Address;
import fcamara.user_address_api.entity.Role;
import fcamara.user_address_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .name("Test User")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(testUser);

        testAddress = Address.builder()
                .street("Rua Teste")
                .number("123")
                .complement("Apto 101")
                .district("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01001000")
                .user(testUser)
                .build();
        addressRepository.save(testAddress);
    }

    @Test
    @DisplayName("Should save and retrieve address by ID and user")
    void shouldSaveAndRetrieveAddressByIdAndUser() {
        Optional<Address> found = addressRepository.findByIdAndUser(testAddress.getId(), testUser);

        assertThat(found).isPresent();
        assertThat(found.get().getStreet()).isEqualTo("Rua Teste");
        assertThat(found.get().getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Should find all addresses by user ID with pagination")
    void shouldFindAllByUserIdWithPagination() {
        Address anotherAddress = Address.builder()
                .street("Avenida Teste")
                .number("456")
                .city("São Paulo")
                .state("SP")
                .zipCode("02002000")
                .user(testUser)
                .build();
        addressRepository.save(anotherAddress);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Address> addressesPage = addressRepository.findAllByUserId(testUser.getId(), pageable);

        assertThat(addressesPage.getContent()).hasSize(2);
        assertThat(addressesPage.getContent())
                .extracting(Address::getStreet)
                .containsExactlyInAnyOrder("Rua Teste", "Avenida Teste");
    }

    @Test
    @DisplayName("Should count addresses by user ID")
    void shouldCountByUserId() {

        long count = addressRepository.countByUserId(testUser.getId());

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find all addresses with pagination")
    void shouldFindAllWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Address> addressesPage = addressRepository.findAll(pageable);

        assertThat(addressesPage.getContent()).hasSize(1);
        assertThat(addressesPage.getContent().get(0).getStreet()).isEqualTo("Rua Teste");
    }

    @Test
    @DisplayName("Should find addresses not belonging to a specific user")
    void shouldFindByUserIdNot() {
        User anotherUser = User.builder()
                .email("another@example.com")
                .name("Another User")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(anotherUser);

        Address anotherAddress = Address.builder()
                .street("Rua Outro")
                .number("789")
                .city("Rio de Janeiro")
                .state("RJ")
                .zipCode("03003000")
                .user(anotherUser)
                .build();
        addressRepository.save(anotherAddress);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Address> addressesPage = addressRepository.findByUserIdNot(testUser.getId(), pageable);

        assertThat(addressesPage.getContent()).hasSize(1);
        assertThat(addressesPage.getContent().get(0).getStreet()).isEqualTo("Rua Outro");
    }

    @Test
    @DisplayName("Should return empty when address not found for user")
    void shouldReturnEmptyWhenAddressNotFoundForUser() {
        Optional<Address> found = addressRepository.findByIdAndUser(UUID.randomUUID(), testUser);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return empty page when no addresses for user")
    void shouldReturnEmptyPageWhenNoAddressesForUser() {
        User newUser = User.builder()
                .email("new@example.com")
                .name("New User")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Address> addressesPage = addressRepository.findAllByUserId(newUser.getId(), pageable);
        assertThat(addressesPage.getContent()).isEmpty();
    }
}
