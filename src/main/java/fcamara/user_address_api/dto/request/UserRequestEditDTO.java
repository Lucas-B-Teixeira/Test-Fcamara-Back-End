package fcamara.user_address_api.dto.request;


import fcamara.user_address_api.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestEditDTO {

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
            description = "The role assigned to the user. Must be either 'USER' or 'ADMIN'.",
            example = "USER"
    )
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(USER|ADMIN)$", message = "Role must be either 'USER' or 'ADMIN'")
    private Role role;
}
