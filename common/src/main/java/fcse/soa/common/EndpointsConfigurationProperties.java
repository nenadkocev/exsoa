package fcse.soa.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "soa.endpoints")
@Getter
@Setter
@Configuration
public class EndpointsConfigurationProperties {

    private String ordesrUrl;

    private String productsUrl;

    private String usersUrl;
}
