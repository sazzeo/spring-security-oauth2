package nextstep.security.oauth2;

import nextstep.security.properties.Registration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public interface OAuth2AccessTokenRequestProvider {
    String getAccessToken(final Registration registration, final String code);

}
