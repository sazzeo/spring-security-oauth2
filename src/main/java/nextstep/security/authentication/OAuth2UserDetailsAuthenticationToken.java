package nextstep.security.authentication;

import nextstep.security.oauth2.userdetails.OAuth2UserDetails;

import java.util.Map;
import java.util.Set;

public class OAuth2UserDetailsAuthenticationToken implements Authentication {

    private final Object principal;
    private final String accessToken;
    private final boolean authenticated;
    private final Set<String> authorities;
    private final OAuth2UserDetails oAuth2UserDetails;

    public OAuth2UserDetailsAuthenticationToken(final Object principal,
                                                final String accessToken,
                                                final boolean authenticated,
                                                final Set<String> authorities,
                                                final OAuth2UserDetails oAuth2UserDetails) {
        this.principal = principal;
        this.accessToken = accessToken;
        this.authenticated = authenticated;
        this.authorities = authorities;
        this.oAuth2UserDetails = oAuth2UserDetails;
    }

    public static OAuth2UserDetailsAuthenticationToken unauthenticated(String principal, String credentials) {
        return new OAuth2UserDetailsAuthenticationToken(principal, credentials, false, Set.of(), null);
    }


    public static OAuth2UserDetailsAuthenticationToken authenticated(String principal,
                                                                     String credentials,
                                                                     Set<String> authorities,
                                                                     final OAuth2UserDetails oAuth2UserDetails) {
        return new OAuth2UserDetailsAuthenticationToken(principal, credentials, true, authorities, oAuth2UserDetails);
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

    public Map<String, String> getOAuth2OtherDetails() {
        return oAuth2UserDetails.getOthers();
    }
}
