package nextstep.app;

import jakarta.annotation.Nullable;
import nextstep.security.properties.ClientRegistrationRepository;
import nextstep.security.properties.OAuth2ClientProperties;
import nextstep.security.properties.Registration;
import org.springframework.stereotype.Component;

@Component
public class InMemoryClientRegistrationRepository implements ClientRegistrationRepository {

    private final OAuth2ClientProperties oAuth2ClientProperties;

    public InMemoryClientRegistrationRepository(final OAuth2ClientProperties oAuth2ClientProperties) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Override
    @Nullable
    public Registration findRegistrationById(final String id) {
        return oAuth2ClientProperties.getRegistration(id);
    }

}
