package fcamara.user_address_api.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "Represents a generic error returned by the API.")
public class BaseBodyError {

    @Schema(description = "Error code", example = "403")
    private String errorCode;

    @Schema(description = "Descriptive error message", example = "You do not have permission to access this user")
    private String message;

    public String toJson() {
        return "(\"status\": " + getErrorCode() + ", " +
                "\"message\": \"" + getMessage() + "\")";
    }
}
