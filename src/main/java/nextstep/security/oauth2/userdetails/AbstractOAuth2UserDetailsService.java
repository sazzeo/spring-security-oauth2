package nextstep.security.oauth2.userdetails;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class AbstractOAuth2UserDetailsService implements OAuth2UserDetailsService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2UserDetails loadUserByAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        try {
            var userResponse = restTemplate.exchange(getUserUrl(), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {
            }).getBody();
            return createOauth2UserDetails(userResponse);
        } catch (Exception ex) {
            throw new IllegalStateException("user 정보를 받아오는데 실패했습니다. " + ex.getMessage());
        }
    }

    protected abstract OAuth2UserDetails createOauth2UserDetails(Map<String, Object> userResponse);

    protected abstract String getUserUrl();

}
