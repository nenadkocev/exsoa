package fcse.soa.users;

import fcse.soa.users.persistence.UserDbEntity;
import fcse.soa.users.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PostConstruct
    private void bootstrapDb() {
        var user = new UserDbEntity();
        user.setUsername("nkocev");
        user.setBalance(500L);
        userRepository.save(user);
    }

    public UserDbEntity getUserByUsername(String username) {
        Optional<UserDbEntity> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updateBalanceFor(String username, Long balance) {
        var user = getUserByUsername(username);
        user.setBalance(balance);
        userRepository.save(user);
    }
}
