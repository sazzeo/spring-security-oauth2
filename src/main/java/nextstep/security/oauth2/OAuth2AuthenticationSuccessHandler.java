package nextstep.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;

import java.io.IOException;

public interface OAuth2AuthenticationSuccessHandler {

    void onSuccess(final HttpServletRequest request, final HttpServletResponse response, Authentication authentication) throws IOException;
}
