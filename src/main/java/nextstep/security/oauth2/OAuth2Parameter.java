package nextstep.security.oauth2;

public enum OAuth2Parameter {

    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    SCOPE("scope"),
    REDIRECT_URL("redirect_uri"),
    RESPONSE_TYPE("response_type"),

    ;

    private final String path;

    OAuth2Parameter(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
