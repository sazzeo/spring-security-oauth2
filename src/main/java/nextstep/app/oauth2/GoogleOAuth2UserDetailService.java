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
public class GoogleOAuth2UserDetailService implements OAuth2UserDetailsService {

    private final RestTemplate restTemplate;
    @Value("${app.oauth2.google.user-url}")
    private String userUrl;

    public GoogleOAuth2UserDetailService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        var picture = (String) userResponse.get("picture");

        return new OAuth2UserDetailsImpl(email, Map.of("name", name,
                "avatarUrl", picture),
                Set.of("USER"));
    }

    @Override
    public String getVendor() {
        return "google";
    }
}
