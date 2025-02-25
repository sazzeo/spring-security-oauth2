package nextstep.security.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class UrlUtilsTest {

    @DisplayName("uri의 마지막 path 를 가져온다")
    @Test
    void getLastPathTest() {
        var request = new MockHttpServletRequest();
        request.setRequestURI("/login/oauth2/code/github");
        assertThat(UrlUtils.getLastPath(request)).isEqualTo("github");
    }

}
