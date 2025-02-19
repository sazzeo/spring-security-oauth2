package nextstep.security.properties;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import org.springframework.http.HttpMethod;

public class Registration {
    private String clientId;
    private String clientSecret;
    private String responseType;
    private String redirectUri;
    private String scope;
    private String authorizeUri;
    private String tokenUri;
    private RequestMatcher requestMatcher;

    public boolean matchRedirectUrl(final HttpServletRequest request) {
        if (requestMatcher == null) {
            return false;
        }
        return requestMatcher.matches(request);
    }

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

    public void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
        this.requestMatcher = new MvcRequestMatcher(HttpMethod.GET, redirectUri);
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

}
