package nextstep.security.properties;

public class Registration {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String redirectUri;
    private String scope;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }
}
