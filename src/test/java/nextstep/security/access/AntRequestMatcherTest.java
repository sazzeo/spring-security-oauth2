package nextstep.security.access;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

class AntRequestMatcherTest {

    @DisplayName("패턴이 /** 로 끝나면 이전 경로가 일치했을때 모두 통과한다")
    @Test
    void matchTest() {
        var matcher = new AntRequestMatcher(HttpMethod.GET, "/url/**");
        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/url/test");
        Assertions.assertThat(matcher.matches(request)).isTrue();
    }
}