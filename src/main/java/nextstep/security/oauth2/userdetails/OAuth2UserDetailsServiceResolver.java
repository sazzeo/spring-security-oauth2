package nextstep.security.oauth2.userdetails;

import java.util.Map;

public class OAuth2UserDetailsServiceResolver {
    private final Map<String, OAuth2UserDetailsService> userDetailsServiceMap;

    public OAuth2UserDetailsServiceResolver(final Map<String, OAuth2UserDetailsService> userDetailsServiceMap) {
        this.userDetailsServiceMap = userDetailsServiceMap;
    }

    public OAuth2UserDetailsService getService(String vendor) {
        return userDetailsServiceMap.get(vendor);
    };
}
