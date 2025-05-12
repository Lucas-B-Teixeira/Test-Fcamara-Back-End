package fcamara.user_address_api.controller.impl;

import fcamara.user_address_api.controller.AddressApi;
import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import fcamara.user_address_api.service.AddressService;
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
public class AddressControllerImpl implements AddressApi {

    private final AddressService addressService;

    public AddressControllerImpl(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public ResponseEntity<AddressResponseDTO> create(
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.createAddress(request, auth));
    }

    @Override
    public ResponseEntity<AddressResponseDTO> addAddressToUserAsAdmin(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressRequestDTO addressDTO,
            Authentication auth
    ) {
        AddressResponseDTO response = addressService.addAddressToUserAsAdmin(userId, addressDTO, auth);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "state") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Authentication auth
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AddressResponseDTO> addressPage = addressService.listAddresses(pageRequest, auth);
        return new ResponseEntity<>(addressPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "state") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Authentication auth
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AddressResponseDTO> addressPage = addressService.getAllAddresses(pageRequest, auth);
        return new ResponseEntity<>(addressPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> listAddressesByUserId(
            @PathVariable UUID userId,
            @ParameterObject Pageable pageable,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.listAddressesByUserId(userId, pageable, auth));
    }

    @Override
    public ResponseEntity<Long> countAddresses(Authentication auth) {
        long count = addressService.countAddress(auth);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<AddressResponseDTO> getById(
            @PathVariable UUID id,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.getAddressById(id, auth));
    }

    @Override
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request,
            Authentication auth
    ) {
        return ResponseEntity.ok(addressService.updateAddress(id, request, auth));
    }

    @Override
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            Authentication auth
    ) {
        addressService.deleteAddress(id, auth);
        return ResponseEntity.noContent().build();
    }
}