package fcamara.user_address_api.service.impl;

import fcamara.user_address_api.client.ViaCepClient;
import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import fcamara.user_address_api.error.exception.forbidden.ForbiddenException;
import fcamara.user_address_api.error.exception.notFound.NotFoundException;
import fcamara.user_address_api.model.Address;
import fcamara.user_address_api.model.Role;
import fcamara.user_address_api.model.User;
import fcamara.user_address_api.repository.AddressRepository;
import fcamara.user_address_api.repository.UserRepository;
import fcamara.user_address_api.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ViaCepClient viaCepClient;

    @Transactional
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        var viaCep = viaCepClient.getAddressFromCep(request.getZipCode());

        Address address = Address.builder()
                .zipCode(request.getZipCode())
                .number(request.getNumber())
                .complement(request.getComplement())
                .street(viaCep.getLogradouro())
                .district(viaCep.getBairro())
                .city(viaCep.getLocalidade())
                .state(viaCep.getUf())
                .user(user)
                .build();

        addressRepository.save(address);

        return toResponse(address);
    }

    @Override
    public Page<AddressResponseDTO> listAddresses(Pageable pageable, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        return addressRepository.findAllByUserId(user.getId(), pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> listAddressesByUserId(UUID userId, Pageable pageable, Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to query all addresses.");
        }

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }

        return addressRepository.findAllByUserId(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    public AddressResponseDTO getAddressById(UUID id, Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to query this address.");
        }

        Address address = addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found."));

        return toResponse(address);
    }

    @Transactional
    @Override
    public AddressResponseDTO updateAddress(UUID id, AddressRequestDTO request, Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to update this address.");
        }

        Address address = addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found."));

        var cepResponse = viaCepClient.getAddressFromCep(request.getZipCode());

        address.setStreet(cepResponse.getLogradouro());
        address.setNumber(request.getNumber());
        address.setComplement(request.getComplement());
        address.setDistrict(cepResponse.getBairro());
        address.setCity(cepResponse.getLocalidade());
        address.setState(cepResponse.getUf());
        address.setZipCode(request.getZipCode());

        Address updated = addressRepository.save(address);
        return toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteAddress(UUID id, Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You do not have permission to delete this address.");
        }

        Address address = addressRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotFoundException("Address not found."));
        addressRepository.delete(address);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    private AddressResponseDTO toResponse(Address address) {
        return AddressResponseDTO.builder()
                .id(address.getId())
                .zipCode(address.getZipCode())
                .number(address.getNumber())
                .complement(address.getComplement())
                .street(address.getStreet())
                .district(address.getDistrict())
                .city(address.getCity())
                .state(address.getState())
                .build();
    }
}
