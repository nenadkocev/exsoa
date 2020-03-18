package fcse.soa.users.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDbEntity, Long> {

    Optional<UserDbEntity> findByUsername(String username);
}
