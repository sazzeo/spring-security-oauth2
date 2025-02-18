package nextstep.security.access;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class AntRequestMatcher implements RequestMatcher {

    private static final String MATCH_ALL = "/**";
    private HttpMethod method;
    private String pattern;

    public AntRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.method != null && !this.method.name().equals(request.getMethod())) {
            return false;
        }

        if (pattern.endsWith(MATCH_ALL)) {
            var extractUrl = pattern.substring(0, pattern.indexOf(MATCH_ALL));
            if (request.getRequestURI().startsWith(extractUrl)) {
                return true;
            }
        }

        return request.getRequestURI().equals(pattern);
    }
}
