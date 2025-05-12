package fcamara.user_address_api.service;

import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.request.UserRequestEditDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import fcamara.user_address_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO getUserById(UUID id, Authentication auth);
    Page<UserResponseDTO> getAllUsers(Pageable pageable, Authentication auth);
    long countUsers(Authentication auth);
    UserResponseDTO updateUser(UUID id, UserRequestEditDTO dto, Authentication auth);
    User getUserByEmail(String email);
    void deleteUser(UUID id, Authentication auth);
}
