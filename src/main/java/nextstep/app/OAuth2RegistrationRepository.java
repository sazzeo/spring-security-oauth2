package nextstep.app;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.properties.OAuth2Properties;
import nextstep.security.properties.Registration;
import org.springframework.stereotype.Component;

@Component
public class OAuth2RegistrationRepository {

    private final OAuth2Properties oAuth2Properties;

    public OAuth2RegistrationRepository(final OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    public Registration getRegistration(final String key) {
        return oAuth2Properties.getRegistration(key);
    }

    @Nullable
    public Registration getRegistrationByRedirectUrl(final HttpServletRequest request) {
        return oAuth2Properties.getRegistrationByRedirectUrl(request);
    }
}
