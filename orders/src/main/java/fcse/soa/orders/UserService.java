package fcse.soa.orders;

import fcse.soa.common.EndpointsConfigurationProperties;
import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public static final String USER = "/user";

    private final EndpointsConfigurationProperties endpoints;
    private final RestTemplate restTemplate;

    UserDbEntity getUserByUsername(String userName) {
        try {
            String URL = endpoints.getUsersUrl() + USER;
            UriComponents url = UriComponentsBuilder.
                    fromHttpUrl(URL)
                    .queryParam("user", userName)
                    .build();
            restTemplate.getForObject(url.toUri(), UserDbEntity.class);
            return restTemplate.getForObject(new URI(URL), UserDbEntity.class);
        } catch (Throwable t) {
            log.error("Cannot found user with username: {}. Thrown exception", userName, t);
        }
        return null;
    }
}