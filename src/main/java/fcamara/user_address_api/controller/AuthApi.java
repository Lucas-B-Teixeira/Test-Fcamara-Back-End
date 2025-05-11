package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.AuthRequestDTO;
import fcamara.user_address_api.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth management operations")
public interface AuthApi {
    @Operation(
            summary = "Login with email and password",
            description = "Authenticates user and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request);
}
