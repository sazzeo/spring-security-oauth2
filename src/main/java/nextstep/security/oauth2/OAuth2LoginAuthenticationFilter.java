package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.AntRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.OAuth2UserDetailsAuthenticationToken;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;
import nextstep.security.properties.ClientRegistrationRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

import static nextstep.security.properties.Registration.REDIRECT_URL_PREFIX;

public class OAuth2LoginAuthenticationFilter extends GenericFilterBean {
    private static final String REDIRECT_MATCH_URL = REDIRECT_URL_PREFIX + "/**";
    private final OAuth2AccessTokenRequestProvider oAuth2AccessTokenRequestProvider = new OAuth2AccessTokenRequestProvider();
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;
    private final RequestMatcher requestMatcher = new AntRequestMatcher(HttpMethod.GET, REDIRECT_MATCH_URL);

    private final AuthorizationRequestRepository authorizationRequestRepository;

    public OAuth2LoginAuthenticationFilter(final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                           final List<OAuth2UserDetailsService> userDetailsServices,
                                           final AuthorizationRequestRepository authorizationRequestRepository
    ) {
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServices);
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!requestMatcher.matches(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var authorizationRequest = authorizationRequestRepository.get(httpServletRequest);
        if(authorizationRequest == null) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }
        var registration = authorizationRequest.getRegistration();

        var code = httpServletRequest.getParameter("code");
        if (code == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var accessToken = oAuth2AccessTokenRequestProvider.getAccessToken(registration, code);
        var oauth2DetailsService = oauth2UserDetailsServiceResolver.getService(registration.getRegistrationId());

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
