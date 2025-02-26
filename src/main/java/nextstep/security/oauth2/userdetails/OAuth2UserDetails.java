package nextstep.security.oauth2.userdetails;

import java.util.Map;
import java.util.Set;

public interface OAuth2UserDetails {
    String getUsername();

    Map<String, String> getOthers();

    Set<String>  getAuthorities();

}
