package nextstep.security.oauth2.userdetails;

public interface OAuth2UserDetailsService {

    OAuth2UserDetails loadUserAccessToken(String accessToken);

}
