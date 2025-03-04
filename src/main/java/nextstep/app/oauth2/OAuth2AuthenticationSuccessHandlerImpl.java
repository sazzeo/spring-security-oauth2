package nextstep.app.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.application.MemberService;
import nextstep.app.payload.Oauth2MemberSaveDto;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.OAuth2UserDetailsAuthenticationToken;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.oauth2.OAuth2AuthenticationSuccessHandler;

import java.io.IOException;
public class OAuth2AuthenticationSuccessHandlerImpl implements OAuth2AuthenticationSuccessHandler {
    private final HttpSessionSecurityContextRepository securityContextRepository;
    private final MemberService memberService;

    public OAuth2AuthenticationSuccessHandlerImpl(final HttpSessionSecurityContextRepository securityContextRepository,
                                                  final MemberService memberService) {
        this.securityContextRepository = securityContextRepository;
        this.memberService = memberService;
    }

    @Override
    public void onSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2UserDetailsAuthenticationToken authToken) {
            var otherDetails = authToken.getOAuth2OtherDetails();
            memberService.saveOauth2Member(new Oauth2MemberSaveDto(
                    (String) authToken.getPrincipal(),
                    otherDetails.get("name"),
                    otherDetails.get("avatar_url")
            ));
        } else {
            throw new IllegalArgumentException("유효한 토큰타입이 아닙니다.");
        }

        SecurityContext context = new SecurityContext(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        response.sendRedirect("/");
    }
}
