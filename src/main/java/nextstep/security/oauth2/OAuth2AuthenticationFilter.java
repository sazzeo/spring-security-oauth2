package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.OAuth2RegistrationRepository;
import nextstep.security.authentication.OAuth2UserDetailsAuthenticationToken;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class OAuth2AuthenticationFilter extends GenericFilterBean {
    private final OAuth2RegistrationRepository oAuth2RegistrationRepository;
    private final OAuth2AccessTokenRequestProvider oAuth2AccessTokenRequestProvider;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;

    public OAuth2AuthenticationFilter(final OAuth2RegistrationRepository oAuth2RegistrationRepository,
                                      final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                      final List<OAuth2UserDetailsService> userDetailsServices
    ) {
        this.oAuth2RegistrationRepository = oAuth2RegistrationRepository;
        this.oAuth2AccessTokenRequestProvider = new OAuth2AccessTokenRequestProviderImpl();
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServices);
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        var registration = oAuth2RegistrationRepository.getRegistrationByRedirectUrl(httpServletRequest);
        if (registration == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var code = httpServletRequest.getParameter("code");
        if (code == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var accessToken = oAuth2AccessTokenRequestProvider.getAccessToken(registration, code);

        var oauth2DetailsService = oauth2UserDetailsServiceResolver.getService(registration.getVendor());

        try {
            var userDetails = oauth2DetailsService.loadUserByAccessToken(accessToken);
            var authenticatedToken = OAuth2UserDetailsAuthenticationToken
                    .authenticated(userDetails.getUsername(), accessToken, userDetails.getAuthorities(), userDetails);
            oAuth2AuthenticationSuccessHandler.onSuccess(httpServletRequest, httpServletResponse, authenticatedToken);
        } catch (Exception ex) {
            //TODO failHandler 구현
        }
    }
}
