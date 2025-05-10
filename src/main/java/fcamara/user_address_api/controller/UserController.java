package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import fcamara.user_address_api.security.service.JwtService;
import fcamara.user_address_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Email already in use or data integrity violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error during creation")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get current logged-in user", description = "Returns the data osf the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or token invalid")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader, Authentication auth) {
        String token = authHeader.replace("Bearer ", "").trim();
        UUID userId = jwtService.extractUserId(token);
        UserResponseDTO user = userService.getUserById(userId, auth); // Assumindo que você tem esse método
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by ID. Users can only fetch their own data, except admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id, Authentication auth) {
        UserResponseDTO userResponseDTO = userService.getUserById(id, auth);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all users", description = "Returns a paginated list of all users. Only admins can access this.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Only admins can access this resource")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            Authentication auth
    ) {
        Page<UserResponseDTO> userPage = userService.getAllUsers(PageRequest.of(page, size, Sort.by(sortBy)), auth);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @Operation(summary = "Update a user", description = "Updates the data of a specific user. Users can only update themselves unless they are admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Email already in use or data integrity violation"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to update this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userRequestDTO, Authentication auth) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO, auth);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID. Users can only delete themselves unless they are admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to delete this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication auth) {
        userService.deleteUser(id, auth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}