package nextstep.app.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandlerImpl implements OAuth2AuthenticationSuccessHandler {
    private final HttpSessionSecurityContextRepository securityContextRepository;

    public OAuth2AuthenticationSuccessHandlerImpl(final HttpSessionSecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void onSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        SecurityContext context = new SecurityContext(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        response.sendRedirect("/");
    }
}
