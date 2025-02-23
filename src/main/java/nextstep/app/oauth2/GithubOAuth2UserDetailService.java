package nextstep.app.oauth2;

import nextstep.security.oauth2.userdetails.OAuth2UserDetails;
import nextstep.security.oauth2.userdetails.OAuth2UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Component
public class GithubOAuth2UserDetailService implements OAuth2UserDetailsService {

    private final RestTemplate restTemplate;

    private final String userUrl;

    public GithubOAuth2UserDetailService(final RestTemplate restTemplate,
                                         @Value("${app.oauth2.github.user-url}") final String userUrl
    ) {
        this.restTemplate = restTemplate;
        this.userUrl = userUrl;
    }

    @Override
    public OAuth2UserDetails loadUserByAccessToken(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        var userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {
        }).getBody();

        var email = (String) userResponse.get("email");
        var name = (String) userResponse.get("name");
        var avatarUrl = (String) userResponse.get("avatar_url");

        return new OAuth2UserDetailsImpl(email, Map.of("name", name,
                "avatarUrl", avatarUrl),
                Set.of("USER"));
    }

    @Override
    public String getVendor() {
        return "github";
    }
}
