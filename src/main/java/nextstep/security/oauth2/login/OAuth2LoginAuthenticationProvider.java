package nextstep.security.oauth2.login;

import jakarta.annotation.Nullable;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authorization.AccessDeniedException;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;
import nextstep.security.properties.Registration;

import java.util.List;

public class OAuth2LoginAuthenticationProvider implements AuthenticationManager {
    private final AuthenticationManager authenticationManager = new OAuth2AuthorizationCodeAuthenticationProvider();
    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;

    public OAuth2LoginAuthenticationProvider(final List<OAuth2UserDetailsService> userDetailsServices) {
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServices);
    }

    @Nullable
    @Override
    public Authentication authenticate(final Authentication authentication) {
        if (authentication instanceof OAuth2LoginAuthenticationToken token) {
            var registration = token.getRegistration();
            var oAuth2AuthorizationCodeAuthenticationToken = authenticationManager.authenticate(OAuth2AuthorizationCodeAuthenticationToken.unauthenticated(token.getPrincipal(), //code가 들어있음
                    registration));
            return createOAuth2LoginAuthenticationTokenResponse(registration, oAuth2AuthorizationCodeAuthenticationToken);
        }
        return null;
    }

    private OAuth2LoginAuthenticationToken createOAuth2LoginAuthenticationTokenResponse(final Registration registration, final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new AccessDeniedException("code 정보로부터 accessToken 을 받아오는데 실패했습니다.");
        }
        if (!(authentication instanceof final OAuth2AuthorizationCodeAuthenticationToken oAuth2AuthorizationCodeAuthenticationToken)) {
            return null;
        }
        var oauth2DetailsService = oauth2UserDetailsServiceResolver.getService(registration.getRegistrationId());

        try {
            var userDetails = oauth2DetailsService.loadUserByAccessToken(oAuth2AuthorizationCodeAuthenticationToken.getAccessToken());
            return OAuth2LoginAuthenticationToken.authenticated(userDetails);
        } catch (Exception ex) {
            throw new AccessDeniedException("accessToken 으로 부터 userInfo를 받아오는데 실패했습니다.");
        }
    }


}
