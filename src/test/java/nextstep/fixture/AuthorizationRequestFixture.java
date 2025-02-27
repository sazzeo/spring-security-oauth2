package nextstep.fixture;

import nextstep.security.oauth2.OAuth2AuthorizationRequest;
import nextstep.security.properties.Registration;

public enum AuthorizationRequestFixture {

    GITHUB {
        @Override
        public OAuth2AuthorizationRequest getOAuth2AuthorizationRequest() {
            var registration = new Registration();
            registration.setDomain("http://localhost:8080");
            registration.setClientSecret("client-secret-test");
            registration.setClientId("client-id-test");
            registration.setAuthorizeUri("https://github.com/login/oauth/authorize");
            registration.setTokenUri("http://localhost:8089/login/oauth/access_token");
            registration.setRegistrationId("github");
            return new OAuth2AuthorizationRequest(registration);
        }
    },
    GOOGLE {
        @Override
        public OAuth2AuthorizationRequest getOAuth2AuthorizationRequest() {
            var registration = new Registration();
            registration.setDomain("http://localhost:8080");
            registration.setClientSecret("client-secret-test");
            registration.setClientId("client-id-test");
            registration.setAuthorizeUri("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
            registration.setTokenUri("http://localhost:8089/token");
            registration.setRegistrationId("google");
            return new OAuth2AuthorizationRequest(registration);
        }
    };

    public abstract OAuth2AuthorizationRequest getOAuth2AuthorizationRequest();
}
