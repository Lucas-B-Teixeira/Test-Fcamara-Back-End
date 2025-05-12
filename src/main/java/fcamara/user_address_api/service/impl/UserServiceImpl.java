package fcamara.user_address_api.service.impl;

import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.request.UserRequestEditDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import fcamara.user_address_api.error.exception.InternalServer.InternalServerException;
import fcamara.user_address_api.error.exception.badRequest.BadRequestException;
import fcamara.user_address_api.error.exception.conflict.ConflictException;
import fcamara.user_address_api.error.exception.forbidden.ForbiddenException;
import fcamara.user_address_api.error.exception.notFound.NotFoundException;
import fcamara.user_address_api.entity.Role;
import fcamara.user_address_api.entity.User;
import fcamara.user_address_api.repository.AddressRepository;
import fcamara.user_address_api.repository.UserRepository;
import fcamara.user_address_api.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        try {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ConflictException("Email is already in use");
            }

            User user = User.builder()
                    .name(dto.getName())
                    .email(dto.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .role(Role.USER)
                    .build();

            User saved = userRepository.save(user);
            return toResponseDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Integrity violation: " + e.getMessage());
        } catch (Exception e) {
            if (e instanceof ConflictException) throw e;
            throw new InternalServerException("Failed to create user");
        }
    }

    @Override
    public UserResponseDTO getUserById(UUID id, Authentication auth) {
        User currentUser = getAuthenticatedUser(auth);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to query this user.");
        }

        return userRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable, Authentication auth) {
        User currentUser = getAuthenticatedUser(auth);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to query all users.");
        }

        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::toResponseDTO);
    }

    @Override
    public long countUsers(Authentication auth) {
        User currentUser = getAuthenticatedUser(auth);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to query all users.");
        }

        return userRepository.count();
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestEditDTO dto, Authentication auth) {
        User currentUser = getAuthenticatedUser(auth);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to update this user.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail().toLowerCase());
        if(currentUser.getRole() == Role.ADMIN){
            user.setRole(dto.getRole());
        }

        try {
            return toResponseDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Integrity violation: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteUser(UUID id, Authentication auth) {
        User currentUser = getAuthenticatedUser(auth);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to delete this user.");
        }

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    private UserResponseDTO toResponseDTO(User user) {
        long addressCount = addressRepository.countByUserId(user.getId());

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .addressCount(addressCount)
                .build();
    }
}