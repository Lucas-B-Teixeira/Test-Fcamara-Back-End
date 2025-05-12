package fcamara.user_address_api.service;

import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.request.UserRequestEditDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import fcamara.user_address_api.error.exception.conflict.ConflictException;
import fcamara.user_address_api.error.exception.forbidden.ForbiddenException;
import fcamara.user_address_api.error.exception.notFound.NotFoundException;
import fcamara.user_address_api.entity.Role;
import fcamara.user_address_api.entity.User;
import fcamara.user_address_api.repository.AddressRepository;
import fcamara.user_address_api.repository.UserRepository;
import fcamara.user_address_api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequestDTO dto = new UserRequestDTO("Test User", "test@example.com", "password");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(dto);

        assertEquals(user.getEmail(), response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailExistsOnCreate() {
        UserRequestDTO dto = new UserRequestDTO("Test User", "test@example.com", "password");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(dto));
    }

    @Test
    void shouldGetUserByIdAsAdmin() {
        UUID id = UUID.randomUUID();
        user.setRole(Role.ADMIN);

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(addressRepository.countByUserId(id)).thenReturn(0L);

        UserResponseDTO result = userService.getUserById(id, authentication);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowWhenNonAdminAccessAnotherUser() {
        UUID id = UUID.randomUUID();
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> userService.getUserById(id, authentication));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UserRequestEditDTO dto = new UserRequestEditDTO("Updated Name", "updated@example.com", null);

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(addressRepository.countByUserId(user.getId())).thenReturn(1L);

        UserResponseDTO response = userService.updateUser(user.getId(), dto, authentication);

        assertEquals(user.getEmail(), response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteUser(user.getId(), authentication);
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void shouldThrowWhenUserToDeleteDoesNotExist() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(user.getId(), authentication));
    }

    @Test
    void shouldThrowWhenNonAdminTriesToGetAllUsers() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> userService.getAllUsers(PageRequest.of(0, 10), authentication));
    }

    @Test
    void shouldReturnUsersPageAsAdmin() {
        user.setRole(Role.ADMIN);
        Page<User> page = new PageImpl<>(List.of(user));

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(addressRepository.countByUserId(user.getId())).thenReturn(1L);

        Page<UserResponseDTO> result = userService.getAllUsers(PageRequest.of(0, 10), authentication);

        assertEquals(1, result.getTotalElements());
    }
}
