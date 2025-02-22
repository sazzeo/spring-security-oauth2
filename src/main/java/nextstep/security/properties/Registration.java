package nextstep.security.properties;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;

public class Registration {
    private String clientId;
    private String clientSecret;
    private String responseType;
    private String redirectUri;
    private String scope;
    private String authorizeUri;
    private String tokenUri;
    private RequestMatcher redirectRequestMatcher;

    private String vendor;

    public boolean matchRedirectUrl(final HttpServletRequest request) {
        if (redirectRequestMatcher == null) {
            return false;
        }
        return redirectRequestMatcher.matches(request);
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
        try {
            var uri = new URI(redirectUri);
            this.redirectRequestMatcher = new MvcRequestMatcher(HttpMethod.GET, uri.getPath());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("oauth2 client redirect-url 이 부적절합니다.");
        }
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
}
