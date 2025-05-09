package fcamara.user_address_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthResponseDTO {

    @Schema(
            description = "The JWT token used for authenticating further requests.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.OaJALpI7b4_zLBVpCIot6cvlYgU4XYxj-YfaZ0kzgeY"
    )
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}
