package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.AntRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.properties.ClientRegistrationRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2AuthorizationRequestRedirectFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;
    private final ClientRegistrationRepository clientRegistrationRepository;


    public OAuth2AuthorizationRequestRedirectFilter(final ClientRegistrationRepository clientRegistrationRepository) {
        this.requestMatcher = new AntRequestMatcher(HttpMethod.GET, "/oauth2/authorization/**");
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (!requestMatcher.matches(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var url = httpServletRequest.getRequestURI();
        var vendor = url.substring(url.lastIndexOf("/") + 1);
        var registration = clientRegistrationRepository.findRegistrationById(vendor);
        if (registration == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        var redirectPath = UriComponentsBuilder.fromHttpUrl(registration.getAuthorizeUri())
                .queryParam(OAuth2Parameter.CLIENT_ID.getPath(), registration.getClientId())
                .queryParam(OAuth2Parameter.RESPONSE_TYPE.getPath(), OAuth2Parameter.RESPONSE_TYPE.getDefaultValue())
                .queryParam(OAuth2Parameter.SCOPE.getPath(), registration.getScope())
                .queryParam(OAuth2Parameter.REDIRECT_URL.getPath(), registration.getRedirectUrl())
                .toUriString();

        httpServletResponse.sendRedirect(redirectPath);
    }

}
