package nextstep.security.oauth2;

import nextstep.security.properties.Registration;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2AuthorizationRequest {

    private final Registration registration;

    public OAuth2AuthorizationRequest(Registration registration) {
        this.registration = registration;
    }

    @NonNull
    public String getRedirectPath() {
        return UriComponentsBuilder.fromHttpUrl(registration.getAuthorizeUri())
                .queryParam(OAuth2Parameter.CLIENT_ID.getPath(), registration.getClientId())
                .queryParam(OAuth2Parameter.RESPONSE_TYPE.getPath(), OAuth2Parameter.RESPONSE_TYPE.getDefaultValue())
                .queryParam(OAuth2Parameter.SCOPE.getPath(), registration.getScope())
                .queryParam(OAuth2Parameter.REDIRECT_URL.getPath(), registration.getRedirectUrl())
                .toUriString();
    }

    public Registration getRegistration() {
        return registration;
    }
}
