package nextstep.app.oauth2;

import nextstep.security.oauth2.userdetails.OAuth2UserDetails;

import java.util.Map;

public record OAuth2UserDetailsImpl(String userName, Map<String, String> details) implements OAuth2UserDetails {

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public Map<String, String> getDetails() {
        return this.details;
    }
}
