package nextstep.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2Properties {
    private Map<String, Registration> registration = new HashMap<>();

    public Map<String, Registration> getRegistration() {
        return registration;
    }

    public void setRegistration(final Map<String, Registration> registration) {
        this.registration = registration;
    }
}
