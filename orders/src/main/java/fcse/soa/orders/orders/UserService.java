package fcse.soa.orders.orders;

import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public static final String USER = "/user";

    public static final String UPDATE_BALANCE = "/user/update-balance";

    private final EndpointsConfigurationProperties endpoints;
    private final RestTemplate restTemplate;

    UserDbEntity getUserByUsername(String user) {
        try {
            String URL = endpoints.getUsersUrl() + USER;
            UriComponents url = UriComponentsBuilder.
                    fromHttpUrl(URL)
                    .queryParam("user", user)
                    .build();
            return restTemplate.getForObject(url.toUri(), UserDbEntity.class);
        } catch (Throwable t) {
            log.error("Cannot find user with username: {}. Thrown exception", user, t);
        }
        return null;
    }

    public void updateUsersBalance(String user, long newBalance) {
        try {
            String URL = endpoints.getUsersUrl() + UPDATE_BALANCE;
            UriComponents url = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("user", user)
                    .build();
            restTemplate.postForEntity(url.toUri(), newBalance, Object.class);
        } catch (Throwable t) {
            log.error("Cannot update users balance for user:{}. New balance: {}", user, newBalance, t);
        }
    }
}
