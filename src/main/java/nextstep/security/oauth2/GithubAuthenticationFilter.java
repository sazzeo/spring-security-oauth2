package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.RequestMatcher;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GithubAuthenticationFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;
    private final HttpSessionSecurityContextRepository securityContextRepository;

    public GithubAuthenticationFilter(final RequestMatcher requestMatcher, final HttpSessionSecurityContextRepository securityContextRepository) {
        this.requestMatcher = requestMatcher;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!requestMatcher.matches(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        var code = httpServletRequest.getParameter("code");
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("필터");
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "Iv23liPQmsAjZIP9QKfp");
        map.put("client_secret", "2e2f64556d42e913f8661973bc374fa76cbd9aa2");
        map.put("code", code);

        var response = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);

        var accessToken = (String) response.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        var userResponse = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class)
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
