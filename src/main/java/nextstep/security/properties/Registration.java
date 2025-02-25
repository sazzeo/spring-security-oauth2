package nextstep.security.properties;

import jakarta.annotation.Nullable;

public class Registration {
    public static final String REDIRECT_URL_PREFIX = "/login/oauth2/code";

    private String clientId;
    private String clientSecret;
    private String responseType;
    private String scope;
    private String authorizeUri;
    private String tokenUri;

    private String vendor;

    private String domain;


    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(final String tokenUri) {
        this.tokenUri = tokenUri;
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

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }

    public void setAuthorizeUri(final String authorizeUri) {
        this.authorizeUri = authorizeUri;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    @Nullable
    public String getGrantType() {
        if (responseType == null) {
            return null;
        }
        if (this.responseType.equals("code")) {
            return "authorization_code";
        }
        return null;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getRedirectUrl() {
        return domain + REDIRECT_URL_PREFIX + "/" + vendor;
    }
}
