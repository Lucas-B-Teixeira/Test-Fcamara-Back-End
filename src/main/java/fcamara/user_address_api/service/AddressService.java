package fcamara.user_address_api.service;

import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.UUID;


public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO request, Authentication authentication);
    AddressResponseDTO addAddressToUserAsAdmin(UUID userId, AddressRequestDTO dto, Authentication auth);
    Page<AddressResponseDTO> listAddresses(Pageable pageable, Authentication authentication);
    Page<AddressResponseDTO> listAddressesByUserId(UUID userId, Pageable pageable, Authentication authentication);
    Page<AddressResponseDTO> getAllAddresses(Pageable pageable, Authentication auth);
    long countAddress(Authentication auth);
    AddressResponseDTO getAddressById(UUID id, Authentication authentication);
    AddressResponseDTO updateAddress(UUID id, AddressRequestDTO request, Authentication authentication);
    void deleteAddress(UUID id, Authentication authentication);
}
