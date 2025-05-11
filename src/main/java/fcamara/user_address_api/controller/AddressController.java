package fcamara.user_address_api.controller;

import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import fcamara.user_address_api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
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
    public ResponseEntity<AddressResponseDTO> addAddressToUserAsAdmin(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressRequestDTO addressDTO,
            Authentication auth
    ) {
        AddressResponseDTO response = addressService.addAddressToUserAsAdmin(userId, addressDTO, auth);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
            summary = "Get all addresses",
            description = "Returns a paginated list of all addresses. Only admins can access this.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addresses successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Only admins can access this resource")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<AddressResponseDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "state") String sortBy,
            Authentication auth
    ) {
        Page<AddressResponseDTO> addressPage = addressService.getAllAddresses(PageRequest.of(page, size, Sort.by(sortBy)), auth);
        return new ResponseEntity<>(addressPage, HttpStatus.OK);
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

    @Operation(summary = "Count addresses", description = "Retorna o número total de endereços. Admins veem todos, usuários veem apenas os próprios.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contagem de endereços obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> countAddresses(Authentication auth) {
        long count = addressService.countAddress(auth);
        return ResponseEntity.ok(count);
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