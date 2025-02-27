package nextstep.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.properties.ClientRegistrationRepository;
import org.springframework.lang.Nullable;

public class OAuth2AuthorizationRequestResolver {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Nullable
    public OAuth2AuthorizationRequest resolve(final HttpServletRequest request) {
        var url = request.getRequestURI();
        var vendor = url.substring(url.lastIndexOf("/") + 1);
        var registration = clientRegistrationRepository.findRegistrationById(vendor);
        if(registration == null) {
            return null;
        }

        return new OAuth2AuthorizationRequest(registration);
    }

}
