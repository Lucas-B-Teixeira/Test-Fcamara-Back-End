package fcamara.user_address_api.service;

import fcamara.user_address_api.client.ViaCepClient;
import fcamara.user_address_api.dto.request.AddressRequestDTO;
import fcamara.user_address_api.dto.response.AddressResponseDTO;
import fcamara.user_address_api.error.exception.forbidden.ForbiddenException;
import fcamara.user_address_api.entity.Address;
import fcamara.user_address_api.entity.Role;
import fcamara.user_address_api.entity.User;
import fcamara.user_address_api.repository.AddressRepository;
import fcamara.user_address_api.repository.UserRepository;
import fcamara.user_address_api.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AddressServiceImplTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private AddressRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .role(Role.USER)
                .build();

        requestDTO = AddressRequestDTO.builder()
                .zipCode("01001-000")
                .number("100")
                .complement("Apt 1")
                .build();

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void createAddress_shouldCreateAndReturnAddress() {
        var viaCep = new ViaCepClient.ViaCepResponse();
        viaCep.setLogradouro("Rua Teste");
        viaCep.setBairro("Centro");
        viaCep.setLocalidade("São Paulo");
        viaCep.setUf("SP");

        when(viaCepClient.getAddressFromCep(requestDTO.getZipCode())).thenReturn(viaCep);
        when(addressRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AddressResponseDTO response = addressService.createAddress(requestDTO, authentication);

        assertNotNull(response);
        assertEquals("01001-000", response.getZipCode());
        verify(addressRepository, times(1)).save(any());
    }

    @Test
    void addAddressToUserAsAdmin_shouldSucceedIfAdmin() {
        user.setRole(Role.ADMIN);
        UUID otherUserId = UUID.randomUUID();

        var viaCep = new ViaCepClient.ViaCepResponse();
        viaCep.setLogradouro("Rua Teste");
        viaCep.setBairro("Centro");
        viaCep.setLocalidade("São Paulo");
        viaCep.setUf("SP");

        User targetUser = User.builder().id(otherUserId).email("target@example.com").build();

        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(targetUser));
        when(viaCepClient.getAddressFromCep(requestDTO.getZipCode())).thenReturn(viaCep);
        when(addressRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AddressResponseDTO response = addressService.addAddressToUserAsAdmin(otherUserId, requestDTO, authentication);

        assertNotNull(response);
        assertEquals("01001-000", response.getZipCode());
        verify(addressRepository).save(any());
    }

    @Test
    void addAddressToUserAsAdmin_shouldThrowIfNotAdmin() {
        UUID userId = UUID.randomUUID();
        assertThrows(ForbiddenException.class, () -> addressService.addAddressToUserAsAdmin(userId, requestDTO, authentication));
    }

    @Test
    void listAddresses_shouldReturnPagedResults() {
        Address address = Address.builder().id(UUID.randomUUID()).user(user).build();
        when(addressRepository.findAllByUserId(eq(user.getId()), any())).thenReturn(new PageImpl<>(List.of(address)));

        Page<AddressResponseDTO> result = addressService.listAddresses(PageRequest.of(0, 10), authentication);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAddressById_shouldReturnAddressIfAdminAndOwner() {
        user.setRole(Role.ADMIN);
        Address address = Address.builder().id(UUID.randomUUID()).user(user).build();

        when(addressRepository.findByIdAndUser(address.getId(), user)).thenReturn(Optional.of(address));

        AddressResponseDTO response = addressService.getAddressById(address.getId(), authentication);
        assertNotNull(response);
    }

    @Test
    void getAddressById_shouldThrowIfNotAdmin() {
        assertThrows(ForbiddenException.class, () -> addressService.getAddressById(UUID.randomUUID(), authentication));
    }

    @Test
    void updateAddress_shouldSucceedForOwner() {
        Address address = Address.builder().id(UUID.randomUUID()).user(user).build();

        var viaCep = new ViaCepClient.ViaCepResponse();
        viaCep.setLogradouro("Rua Atualizada");
        viaCep.setBairro("Novo Bairro");
        viaCep.setLocalidade("São Paulo");
        viaCep.setUf("SP");

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        when(viaCepClient.getAddressFromCep(any())).thenReturn(viaCep);
        when(addressRepository.save(any())).thenReturn(address);

        AddressResponseDTO response = addressService.updateAddress(address.getId(), requestDTO, authentication);
        assertNotNull(response);
    }

    @Test
    void deleteAddress_shouldSucceedForOwner() {
        Address address = Address.builder().id(UUID.randomUUID()).user(user).build();

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        assertDoesNotThrow(() -> addressService.deleteAddress(address.getId(), authentication));
        verify(addressRepository).delete(address);
    }

    @Test
    void deleteAddress_shouldThrowIfNotOwnerOrAdmin() {
        Address address = Address.builder().id(UUID.randomUUID()).user(User.builder().id(UUID.randomUUID()).build()).build();
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        assertThrows(ForbiddenException.class, () -> addressService.deleteAddress(address.getId(), authentication));
    }
}
