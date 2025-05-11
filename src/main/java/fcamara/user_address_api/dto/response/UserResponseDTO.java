package fcamara.user_address_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDTO {

    @Schema(
            description = "The unique identifier for the user.",
            example = "f1a5bc2d-47d7-4c9c-b9a5-b7e5d4f7e96e"
    )
    private UUID id;

    @Schema(
            description = "The full name of the user.",
            example = "John Doe"
    )
    private String name;

    @Schema(
            description = "The email address of the user.",
            example = "john.doe@example.com"
    )
    private String email;

    @Schema(
            description = "The role assigned to the user (e.g., USER, ADMIN).",
            example = "USER"
    )
    private String role;

    @Schema(
            description = "The number of addresses associated with the user.",
            example = "3"
    )
    private long addressCount;

    @Schema(
            description = "The date and time when the user was created.",
            example = "2025-05-08T14:35:00"
    )
    private LocalDateTime createdAt;
}
