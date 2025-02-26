package nextstep.security.access;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public class AntRequestMatcher implements RequestMatcher {
    private final HttpMethod method;
    private final String pattern;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AntRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.method != null && !this.method.name().equals(request.getMethod())) {
            return false;
        }
        String requestURI = request.getRequestURI();
        return antPathMatcher.match(pattern, requestURI);
    }
}
