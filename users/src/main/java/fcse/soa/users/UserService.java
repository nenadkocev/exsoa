package fcse.soa.users;

import fcse.soa.common.ResourceNotFoundException;
import fcse.soa.users.persistence.UserDbEntity;
import fcse.soa.users.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDbEntity getUserByUsername(String username) {
        Optional<UserDbEntity> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
