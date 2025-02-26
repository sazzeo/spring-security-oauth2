package nextstep.app.oauth2;

import nextstep.security.oauth2.userdetails.AbstractOAuth2UserDetailsService;
import nextstep.security.oauth2.userdetails.OAuth2UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class GoogleOAuth2UserDetailService extends AbstractOAuth2UserDetailsService {
    private final String userUrl;

    public GoogleOAuth2UserDetailService(@Value("${app.oauth2.google.user-url}") final String userUrl) {
        this.userUrl = userUrl;
    }

    @Override
    protected OAuth2UserDetails createOauth2UserDetails(final Map<String, Object> userResponse) {
        var email = (String) userResponse.get("email");
        var name = (String) userResponse.get("name");
        var picture = (String) userResponse.get("picture");

        return new OAuth2UserDetailsImpl(email, Map.of("name", name, "avatarUrl", picture), Set.of("USER"));

    }

    @Override
    protected String getUserUrl() {
        return this.userUrl;
    }

    @Override
    public String getVendor() {
        return "google";
    }
}
