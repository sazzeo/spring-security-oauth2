package nextstep.security.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationTest {

    @DisplayName("uri path 가 일치하면 true 를 반환한다")
    @Test
    void test() {
        var registration = new Registration();
        registration.setRedirectUri("http://localhost:8080/oauth2/test");

        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/oauth2/test");

        assertThat(registration.matchRedirectUrl(request)).isTrue();
    }

}
