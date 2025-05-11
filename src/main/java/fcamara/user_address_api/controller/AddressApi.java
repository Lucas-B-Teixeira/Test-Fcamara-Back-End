package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/address")
@Tag(name = "Address", description = "Address management operations")
public interface AddressApi {

    @Operation(
            summary = "Create a new address",
            description = "Creates a new address for the authenticated user using the ZIP code to auto-fill address details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    ResponseEntity<AddressResponseDTO> create(
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    );

    @Operation(
            summary = "Add address to user (ADMIN only)",
            description = "Allows an admin to add an address to a specific user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address successfully created"),
            @ApiResponse(responseCode = "403", description = "Only admins can access this resource"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/user/{userId}")
    ResponseEntity<AddressResponseDTO> addAddressToUserAsAdmin(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressRequestDTO addressDTO,
            Authentication auth
    );

    @Operation(
            summary = "Get paginated addresses",
            description = "Retrieves a paginated list of addresses for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    ResponseEntity<Page<AddressResponseDTO>> list(
            @ParameterObject Pageable pageable,
            Authentication auth
    );

    @Operation(
            summary = "Get all addresses",
            description = "Returns a paginated list of all addresses. Only admins can access this.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addresses successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Only admins can access this resource")
    })
    @GetMapping("/all")
    ResponseEntity<Page<AddressResponseDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "state") String sortBy,
            Authentication auth
    );

    @Operation(
            summary = "Admin: Get all addresses by user ID",
            description = "Retrieves all addresses for a specific user ID. Only accessible by admin users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<Page<AddressResponseDTO>> listAddressesByUserId(
            @PathVariable UUID userId,
            @ParameterObject Pageable pageable,
            Authentication auth
    );

    @Operation(summary = "Count addresses", description = "Retorna o número total de endereços. Admins veem todos, usuários veem apenas os próprios.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contagem de endereços obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso")
    })
    @GetMapping("/count")
    ResponseEntity<Long> countAddresses(Authentication auth);

    @Operation(
            summary = "Get address by ID",
            description = "Fetches a single address by its ID for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    ResponseEntity<AddressResponseDTO> getById(
            @PathVariable UUID id,
            Authentication auth
    );

    @Operation(
            summary = "Update an address",
            description = "Updates an existing address for the authenticated user using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    ResponseEntity<AddressResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    );

    @Operation(
            summary = "Delete an address",
            description = "Deletes an address by ID for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            Authentication auth
    );
}
