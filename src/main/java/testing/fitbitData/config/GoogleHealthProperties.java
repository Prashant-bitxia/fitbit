package testing.fitbitData.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google.health")
public class GoogleHealthProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizeUrl;
    private String tokenUrl;
    private String apiBaseUrl;
    private String scopes;
}