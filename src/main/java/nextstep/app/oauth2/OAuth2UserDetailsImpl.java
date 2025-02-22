package nextstep.app.oauth2;

import nextstep.security.oauth2.userdetails.OAuth2UserDetails;

import java.util.Map;
import java.util.Set;

public record OAuth2UserDetailsImpl(String userName, Map<String, String> details, Set<String> authorities) implements OAuth2UserDetails {

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public Map<String, String> getOthers() {
        return this.details;
    }

    @Override
    public Set<String> getAuthorities() {
        return this.authorities;
    }
}
