package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import fcamara.user_address_api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

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
    public ResponseEntity<AddressResponseDTO> create(
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.createAddress(request, auth));
    }

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
    public ResponseEntity<Page<AddressResponseDTO>> list(
            @ParameterObject Pageable pageable,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.listAddresses(pageable, auth));
    }

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
    public ResponseEntity<Page<AddressResponseDTO>> listAddressesByUserId(
            @PathVariable UUID userId,
            @ParameterObject Pageable pageable,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.listAddressesByUserId(userId, pageable, auth));
    }

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
    public ResponseEntity<AddressResponseDTO> getById(
            @PathVariable UUID id,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.getAddressById(id, auth));
    }

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
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.updateAddress(id, request, auth));
    }

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
    ) {
        addressService.deleteAddress(id, auth);
        return ResponseEntity.noContent().build();
    }
}