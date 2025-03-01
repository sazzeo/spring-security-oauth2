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
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.oauth2.login.OAuth2LoginAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import static nextstep.security.properties.Registration.REDIRECT_URL_PREFIX;

public class OAuth2LoginAuthenticationFilter extends GenericFilterBean {
    private static final String REDIRECT_MATCH_URL = REDIRECT_URL_PREFIX + "/**";
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final RequestMatcher requestMatcher = new AntRequestMatcher(HttpMethod.GET, REDIRECT_MATCH_URL);
    private final AuthorizationRequestRepository authorizationRequestRepository;

    private final AuthenticationManager authenticationManager;


    public OAuth2LoginAuthenticationFilter(final AuthenticationSuccessHandler authenticationSuccessHandler,
                                           final AuthorizationRequestRepository authorizationRequestRepository,
                                           final AuthenticationManager authenticationManager
    ) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!requestMatcher.matches(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // request에서 parameter를 가져오기
        var code = httpServletRequest.getParameter("code");
        if (code == null) {
            throw new AuthenticationException("인증 코드 정보가 존재하지 않습니다.");
        }

        // session에서 authorizationRequest를 가져오기
        var authorizationRequest = authorizationRequestRepository.get(httpServletRequest);
        if (authorizationRequest == null) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        var authenticated = authenticationManager.authenticate(
                OAuth2LoginAuthenticationToken.unauthenticated(
                        code,
                        authorizationRequest
                )
        );

        if (authenticated == null || !authenticated.isAuthenticated()) {
            throw new AccessDeniedException("oauth2 인증에 실패했습니다.");
        }

        if (authenticated instanceof OAuth2LoginAuthenticationToken authenticationToken) {
            authenticationSuccessHandler.onSuccess(httpServletRequest, httpServletResponse, authenticationToken);
        }
    }
}
