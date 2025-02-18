package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class GithubLoginRedirectFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;

    public GithubLoginRedirectFilter(final RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        if (!requestMatcher.matches((HttpServletRequest) servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.sendRedirect("https://github.com/login/oauth/authorize" +
                "?client_id=Iv23liPQmsAjZIP9QKfp" +
                "&response_type=code" +
                "&scope=user" +
                "&redirect_uri=http://localhost:8080/login/oauth2/code/github");
    }

}
