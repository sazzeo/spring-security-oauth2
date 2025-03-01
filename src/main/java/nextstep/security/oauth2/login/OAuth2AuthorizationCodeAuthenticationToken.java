package nextstep.security.oauth2.login;

import nextstep.security.authentication.Authentication;
import nextstep.security.properties.Registration;

import java.util.Set;

public class OAuth2AuthorizationCodeAuthenticationToken implements Authentication {

    private final String code;

    private final boolean authenticated;

    private final Set<String> authorities;

    private final Registration registration;

    private final String accessToken;


    private OAuth2AuthorizationCodeAuthenticationToken(final String code,
                                                       final boolean authenticated,
                                                       final Set<String> authorities,
                                                       final Registration registration,
                                                       final String accessToken) {
        this.code = code;
        this.authenticated = authenticated;
        this.authorities = authorities;
        this.registration = registration;
        this.accessToken = accessToken;
    }

    public static OAuth2AuthorizationCodeAuthenticationToken unauthenticated(final String code,
                                                                             final Set<String> authorities,
                                                                             final Registration registration

    ) {
        return new OAuth2AuthorizationCodeAuthenticationToken(code, false, authorities, registration, null);
    }

    public static OAuth2AuthorizationCodeAuthenticationToken authenticated(String code,
                                                                           Set<String> authorities,
                                                                           final Registration registration,
                                                                           final String accessToken) {
        return new OAuth2AuthorizationCodeAuthenticationToken(code, true, authorities, registration, accessToken);
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
}
