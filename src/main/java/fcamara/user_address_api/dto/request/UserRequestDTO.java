package fcamara.user_address_api.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @Schema(
            description = "The full name of the user.",
            example = "John Doe"
    )
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(
            description = "The email address of the user. It must be in a valid email format.",
            example = "john.doe@example.com"
    )
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(
            description = "The password for the user's account. It must have at least 6 characters.",
            example = "SecurePass123",
            minLength = 6
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
