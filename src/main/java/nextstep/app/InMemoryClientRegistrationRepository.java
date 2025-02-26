package nextstep.app;

import jakarta.annotation.Nullable;
import nextstep.security.properties.ClientRegistrationRepository;
import nextstep.security.properties.OAuth2Properties;
import nextstep.security.properties.Registration;
import org.springframework.stereotype.Component;

@Component
public class InMemoryClientRegistrationRepository implements ClientRegistrationRepository {

    private final OAuth2Properties oAuth2Properties;

    public InMemoryClientRegistrationRepository(final OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Override
    @Nullable
    public Registration findRegistrationById(final String id) {
        return oAuth2Properties.getRegistration(id);
    }

}
