package fcamara.user_address_api.repository;

import fcamara.user_address_api.model.Address;
import fcamara.user_address_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Page<Address> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Address> findByIdAndUser(UUID id, User user);
}
