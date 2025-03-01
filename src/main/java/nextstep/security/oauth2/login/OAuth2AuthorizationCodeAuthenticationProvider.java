package nextstep.security.oauth2.login;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.oauth2.OAuth2AccessTokenRequestProvider;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuth2AuthorizationCodeAuthenticationProvider implements AuthenticationManager {
    private final Map<String, OAuth2UserDetailsService> userDetailsServiceMap;
    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;
    private final OAuth2AccessTokenRequestProvider oAuth2AccessTokenRequestProvider = new OAuth2AccessTokenRequestProvider();

    public OAuth2AuthorizationCodeAuthenticationProvider(final List<OAuth2UserDetailsService> userDetailsServices) {
        this.userDetailsServiceMap = userDetailsServices.stream()
                .collect(Collectors.toMap(OAuth2UserDetailsService::getRegistrationId, it -> it));
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServices);
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        if (authentication instanceof OAuth2AuthorizationCodeAuthenticationToken token) {
            return createOAuth2AuthorizationCodeAuthenticationToken(token);
        }
        return null;
    }

    private OAuth2AuthorizationCodeAuthenticationToken createOAuth2AuthorizationCodeAuthenticationToken(final OAuth2AuthorizationCodeAuthenticationToken token) {
        try {
            var accessToken = oAuth2AccessTokenRequestProvider.getAccessToken(
                    token.getRegistration(), token.getPrincipal());

            return OAuth2AuthorizationCodeAuthenticationToken.authenticated(
                    token.getPrincipal(),
                    token.getAuthorities(),
                    token.getRegistration(),
                    accessToken);
        } catch (Exception ex) {
            return OAuth2AuthorizationCodeAuthenticationToken.unauthenticated(
                    token.getPrincipal(),
                    token.getAuthorities(),
                    token.getRegistration());
        }
    }


}
