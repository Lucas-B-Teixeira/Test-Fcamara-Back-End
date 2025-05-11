package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.UserRequestDTO;
import fcamara.user_address_api.dto.request.UserRequestEditDTO;
import fcamara.user_address_api.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management operations")
public interface UserApi {
    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Email already in use or data integrity violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error during creation")
    })
    @PostMapping
    ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO);

    @Operation(summary = "Get current logged-in user", description = "Returns the data osf the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or token invalid")
    })
    @GetMapping("/me")
    ResponseEntity<UserResponseDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader, Authentication auth);

    @Operation(summary = "Get user by ID", description = "Retrieves a user by ID. Users can only fetch their own data, except admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to access this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id, Authentication auth);

    @Operation(summary = "Get all users", description = "Returns a paginated list of all users. Only admins can access this.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Only admins can access this resource")
    })
    @GetMapping
    ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            Authentication auth
    );

    @Operation(summary = "Get total number of users", description = "Returns the total number of registered users.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count successfully retrieved")
    })
    @GetMapping("/count")
    ResponseEntity<Long> getUserCount(Authentication auth);

    @Operation(summary = "Update a user", description = "Updates the data of a specific user. Users can only update themselves unless they are admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Email already in use or data integrity violation"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to update this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestEditDTO userRequestEditDTO, Authentication auth);

    @Operation(summary = "Delete a user", description = "Deletes a user by ID. Users can only delete themselves unless they are admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "403", description = "You do not have permission to delete this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication auth);
}
