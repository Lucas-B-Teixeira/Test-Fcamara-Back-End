package fcamara.user_address_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddressResponseDTO {

    @Schema(
            description = "The unique identifier for the address.",
            example = "f1a5bc2d-47d7-4c9c-b9a5-b7e5d4f7e96e"
    )
    private UUID id;

    @Schema(
            description = "The street name of the address.",
            example = "Rua das Flores"
    )
    private String street;

    @Schema(
            description = "The number of the address (e.g., apartment or house number).",
            example = "123"
    )
    private String number;

    @Schema(
            description = "Additional information about the address (e.g., apartment number, suite).",
            example = "Apt 201"
    )
    private String complement;

    @Schema(
            description = "The district/neighborhood of the address.",
            example = "Jardim das Palmas"
    )
    private String district;

    @Schema(
            description = "The city where the address is located.",
            example = "São Paulo"
    )
    private String city;

    @Schema(
            description = "The state/province where the address is located.",
            example = "SP"
    )
    private String state;

    @Schema(
            description = "The postal code (ZIP Code) of the address.",
            example = "12345-678"
    )
    private String zipCode;
}
