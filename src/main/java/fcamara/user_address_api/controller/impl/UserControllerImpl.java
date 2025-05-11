package fcamara.user_address_api.controller.impl;

import fcamara.user_address_api.controller.UserApi;
import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.request.UserRequestEditDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import fcamara.user_address_api.security.service.JwtService;
import fcamara.user_address_api.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserControllerImpl implements UserApi {

    private final UserService userService;
    private final JwtService jwtService;

    public UserControllerImpl(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader, Authentication auth) {
        String token = authHeader.replace("Bearer ", "").trim();
        UUID userId = jwtService.extractUserId(token);
        UserResponseDTO user = userService.getUserById(userId, auth); // Assumindo que você tem esse método
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id, Authentication auth) {
        UserResponseDTO userResponseDTO = userService.getUserById(id, auth);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            Authentication auth
    ) {
        Page<UserResponseDTO> userPage = userService.getAllUsers(PageRequest.of(page, size, Sort.by(sortBy)), auth);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getUserCount(Authentication auth) {
        long count = userService.countUsers(auth);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestEditDTO userRequestEditDTO, Authentication auth) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestEditDTO, auth);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication auth) {
        userService.deleteUser(id, auth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}