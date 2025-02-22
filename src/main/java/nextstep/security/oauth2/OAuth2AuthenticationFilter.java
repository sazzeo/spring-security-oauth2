package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.OAuth2RegistrationRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class OAuth2AuthenticationFilter extends GenericFilterBean {
    private final HttpSessionSecurityContextRepository securityContextRepository;
    private final OAuth2RegistrationRepository oAuth2RegistrationRepository;
    private final OAuth2AccessTokenRequestProvider oAuth2AccessTokenRequestProvider;

    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;

    private final RestTemplate restTemplate = new RestTemplate();

    public OAuth2AuthenticationFilter(final HttpSessionSecurityContextRepository securityContextRepository,
                                      final OAuth2RegistrationRepository oAuth2RegistrationRepository,
                                      final Map<String, OAuth2UserDetailsService> userDetailsServiceMap) {
        this.securityContextRepository = securityContextRepository;
        this.oAuth2RegistrationRepository = oAuth2RegistrationRepository;
        this.oAuth2AccessTokenRequestProvider = new OAuth2AccessTokenRequestProviderImpl();
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServiceMap);
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

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        var userResponse = restTemplate
                .exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class)
                .getBody();

        var email = (String) userResponse.get("email");
        var name = userResponse.get("name");
        var avatarUrl = userResponse.get("avatar_url");

        Authentication usernamePasswordToken = UsernamePasswordAuthenticationToken.authenticated(
                email,
                accessToken,
                Set.of("USER")
        );

        SecurityContext context = new SecurityContext(usernamePasswordToken);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
