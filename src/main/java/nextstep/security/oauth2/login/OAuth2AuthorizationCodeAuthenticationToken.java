package nextstep.security.oauth2.login;

import nextstep.security.authentication.Authentication;
import nextstep.security.properties.Registration;

import java.util.Set;

public class OAuth2AuthorizationCodeAuthenticationToken implements Authentication {

    private final String code;

    private final boolean authenticated;

    private final Registration registration;

    private final String accessToken;


    private OAuth2AuthorizationCodeAuthenticationToken(final String code,
                                                       final boolean authenticated,
                                                       final Registration registration,
                                                       final String accessToken) {
        this.code = code;
        this.authenticated = authenticated;
        this.registration = registration;
        this.accessToken = accessToken;
    }

    public static OAuth2AuthorizationCodeAuthenticationToken unauthenticated(final String code,
                                                                             final Registration registration

    ) {
        return new OAuth2AuthorizationCodeAuthenticationToken(code, false, registration, null);
    }

    public static OAuth2AuthorizationCodeAuthenticationToken authenticated(String code,
                                                                           final Registration registration,
                                                                           final String accessToken) {
        return new OAuth2AuthorizationCodeAuthenticationToken(code, true, registration, accessToken);
    }

    @Override
    public Set<String> getAuthorities() {
        return null;
    }

    @Override
    public String getCredentials() {
        return this.code;
    }

    @Override
    public String getPrincipal() {
        return this.code;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public Registration getRegistration() {
        return registration;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
