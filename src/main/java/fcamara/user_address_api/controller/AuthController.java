package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.AuthRequestDTO;
import fcamara.user_address_api.dto.response.AuthResponseDTO;
import fcamara.user_address_api.model.User;
import fcamara.user_address_api.security.service.JwtService;
import fcamara.user_address_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userService.getUserByEmail(request.getEmail());
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

}
