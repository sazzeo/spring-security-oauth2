package nextstep.security.properties;

import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2Properties {
    private Map<String, Registration> registration = new HashMap<>();

    @Nullable
    public Registration getRegistration(final String key) {
        return registration.get(key);
    }

    public void setRegistration(final Map<String, Registration> registration) {
        this.registration = registration;
    }
}
