package nextstep.security.properties;

public class Registration {
    private String clientId;
    private String clientSecret;
    private String responseType;
    private String redirectUri;
    private String scope;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setResponseType(final String responseType) {
        this.responseType = responseType;
    }

    public void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }
}
