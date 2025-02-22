package nextstep.security.authentication;

import java.util.Set;

public class OAuth2UserDetailsAuthenticationToken implements Authentication {

    private final Object principal;
    private final String accessToken;
    private final boolean authenticated;
    private final Set<String> authorities;

    public OAuth2UserDetailsAuthenticationToken(final Object principal, final String accessToken, final boolean authenticated, final Set<String> authorities) {
        this.principal = principal;
        this.accessToken = accessToken;
        this.authenticated = authenticated;
        this.authorities = authorities;
    }

    public static OAuth2UserDetailsAuthenticationToken unauthenticated(String principal, String credentials) {
        return new OAuth2UserDetailsAuthenticationToken(principal, credentials, false, Set.of());
    }


    public static OAuth2UserDetailsAuthenticationToken authenticated(String principal, String credentials, Set<String> authorities) {
        return new OAuth2UserDetailsAuthenticationToken(principal, credentials, true, authorities);
    }

    @Override
    public Set<String> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }
}
