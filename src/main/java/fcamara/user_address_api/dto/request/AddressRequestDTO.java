package fcamara.user_address_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating or updating an address.
 * This class represents the input data required to create or update an address.
 * All fields must be provided, except for the complement.
 */
@Getter
@Setter
public class AddressRequestDTO {

    @Schema(
            description = "The postal code (ZIP Code) of the address.",
            example = "12345-678",
            minLength = 8,
            maxLength = 9
    )
    @NotBlank(message = "ZIP Code must not be blank")
    private String zipCode;

    @Schema(
            description = "The number of the address (e.g., house number or apartment number).",
            example = "123",
            minLength = 1
    )
    @NotBlank(message = "Number must not be blank")
    private String number;

    @Schema(
            description = "Additional address information (e.g., apartment, suite). This field is optional.",
            example = "Apt. 101"
    )
    private String complement;
}