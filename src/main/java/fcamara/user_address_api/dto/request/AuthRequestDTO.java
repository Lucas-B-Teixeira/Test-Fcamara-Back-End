package fcamara.user_address_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    @Schema(
            description = "The email of the user for authentication.",
            example = "user@example.com"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "The password associated with the user's account.",
            example = "Password123"
    )
    @NotBlank(message = "Password is required")
    private String password;
}