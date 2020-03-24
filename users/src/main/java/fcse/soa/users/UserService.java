package fcse.soa.users;

import fcse.soa.users.persistence.UserDbEntity;
import fcse.soa.users.persistence.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CircuitBreaker circuitBreaker;

    public UserService(UserRepository userRepository, CircuitBreakerFactory cbf) {
        this.userRepository = userRepository;
        this.circuitBreaker = cbf.create("red");
    }

    @PostConstruct
    private void bootstrapDb() {
        var user = new UserDbEntity();
        user.setUsername("nkocev");
        user.setBalance(500L);
        userRepository.save(user);
    }

    public UserDbEntity getUserByUsername(String username) {
        return circuitBreaker.run(() -> {
            succeedOrNot(username);
            Optional<UserDbEntity> user = userRepository.findByUsername(username);
            return user.orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }, this::fallback);
    }

    @SneakyThrows
    private void succeedOrNot(String username) {
        if(username.equals("dbException")) {
            throw new RuntimeException("Exception thrown while trying to get user...");
        }
        if(username.equals("dbSlowCall")) {
            Thread.sleep(5000);
        }
    }

    public void updateBalanceFor(String username, Long balance) {
        var user = getUserByUsername(username);
        user.setBalance(balance);
        userRepository.save(user);
    }

    public UserDbEntity fallback(Throwable t) {
        log.error("Failed to get user from db ", t);
        return null;
    }
}
