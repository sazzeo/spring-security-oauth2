package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.security.access.AntRequestMatcher;
import nextstep.security.access.RequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class OAuth2RedirectFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;

    public OAuth2RedirectFilter(RequestMatcher requestMatcher) {
        this.requestMatcher = new AntRequestMatcher(HttpMethod.GET, "/oauth2/authorization/**");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    }
}
