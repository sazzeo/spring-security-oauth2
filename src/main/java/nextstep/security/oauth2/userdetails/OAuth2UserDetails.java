package nextstep.security.oauth2.userdetails;

import java.util.Map;

public interface OAuth2UserDetails {
    String getUsername();

    Map<String, String> getDetails();

}
