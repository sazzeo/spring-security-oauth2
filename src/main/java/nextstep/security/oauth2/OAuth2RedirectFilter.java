package nextstep.security.oauth2;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.AntRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.app.OAuth2RegistrationRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2RedirectFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;
    private final OAuth2RegistrationRepository oAuth2RegistrationRepository;


    public OAuth2RedirectFilter(final OAuth2RegistrationRepository oAuth2RegistrationRepository) {
        this.requestMatcher = new AntRequestMatcher(HttpMethod.GET, "/oauth2/authorization/**");
        this.oAuth2RegistrationRepository = oAuth2RegistrationRepository;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (!requestMatcher.matches(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        StringBuffer url = httpServletRequest.getRequestURL();
        var vendor = url.substring(url.lastIndexOf("/") + 1);
        var registration = oAuth2RegistrationRepository.getRegistration(vendor);
        if (registration == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        var redirectPath = UriComponentsBuilder.fromHttpUrl(registration.getAuthorizeUri())
                .queryParam(OAuth2Parameter.CLIENT_ID.getPath(), registration.getClientId())
                .queryParam(OAuth2Parameter.RESPONSE_TYPE.getPath(), registration.getResponseType())
                .queryParam(OAuth2Parameter.SCOPE.getPath(), registration.getScope())
                .queryParam(OAuth2Parameter.REDIRECT_URL.getPath(), registration.getRedirectUri())
                .toUriString();

        httpServletResponse.sendRedirect(redirectPath);
    }


}
