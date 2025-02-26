package nextstep.security.oauth2;

public enum OAuth2Parameter {

    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    SCOPE("scope"),
    REDIRECT_URL("redirect_uri"),
    RESPONSE_TYPE("response_type", OAuth2Parameter.CODE_VALUE),
    CODE("code"),
    GRANT_TYPE("grant_type", OAuth2Parameter.AUTHORIZATION_CODE_VALUE);

    private final String path;
    private final String defaultValue;

    private static final String CODE_VALUE = "code";
    private static final String AUTHORIZATION_CODE_VALUE = "authorization_code";

    OAuth2Parameter(final String path) {
        this.path = path;
        this.defaultValue = null;
    }

    OAuth2Parameter(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
