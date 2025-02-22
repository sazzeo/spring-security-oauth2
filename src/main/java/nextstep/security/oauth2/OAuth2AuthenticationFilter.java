package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.OAuth2RegistrationRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.OAuth2UserDetailsAuthenticationToken;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsServiceResolver;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class OAuth2AuthenticationFilter extends GenericFilterBean {
    private final HttpSessionSecurityContextRepository securityContextRepository;
    private final OAuth2RegistrationRepository oAuth2RegistrationRepository;
    private final OAuth2AccessTokenRequestProvider oAuth2AccessTokenRequestProvider;

    private final OAuth2UserDetailsServiceResolver oauth2UserDetailsServiceResolver;

    public OAuth2AuthenticationFilter(final HttpSessionSecurityContextRepository securityContextRepository, final OAuth2RegistrationRepository oAuth2RegistrationRepository, final List<OAuth2UserDetailsService> userDetailsServices) {
        this.securityContextRepository = securityContextRepository;
        this.oAuth2RegistrationRepository = oAuth2RegistrationRepository;
        this.oAuth2AccessTokenRequestProvider = new OAuth2AccessTokenRequestProviderImpl();
        this.oauth2UserDetailsServiceResolver = new OAuth2UserDetailsServiceResolver(userDetailsServices);
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

        var userDetails = oauth2DetailsService.loadUserByAccessToken(accessToken);

        Authentication usernamePasswordToken = OAuth2UserDetailsAuthenticationToken.authenticated(userDetails.getUsername(), accessToken, Set.of("USER"));

        SecurityContext context = new SecurityContext(usernamePasswordToken);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        httpServletResponse.sendRedirect("/");
    }
}
