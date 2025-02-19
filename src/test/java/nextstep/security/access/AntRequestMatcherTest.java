package nextstep.security.access;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.*;

class AntRequestMatcherTest {

    @DisplayName("패턴이 /** 로 끝나면 이전 경로가 일치했을때 모두 통과한다")
    @ParameterizedTest
    @ValueSource(strings = {"/url/a", "/url/a/b", "/url"})
    void matchTest(String url) {
        var matcher = new AntRequestMatcher(HttpMethod.GET, "/url/**");
        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(url);
        assertThat(matcher.matches(request)).isTrue();
    }


    @DisplayName("패턴에 /* 가 들어가면 1뎁스 일치할 때 통과한다")
    @ParameterizedTest
    @ValueSource(strings = {"/url/a", "/url/b", "/url/c"})
    void matchTest2(String url) {
        var matcher = new AntRequestMatcher(HttpMethod.GET, "/url/*");
        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(url);
        assertThat(matcher.matches(request)).isTrue();
    }

    @DisplayName("패턴에 /* 가 들어가면 2뎁스 이상이거나 이하뎁스 일때 때 통과하지 못한다")
    @ParameterizedTest
    @ValueSource(strings = {"/url/a/b/c", "/url/b/c", "/url"})
    void matchTest3(String url) {
        var matcher = new AntRequestMatcher(HttpMethod.GET, "/url/*");
        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(url);
        assertThat(matcher.matches(request)).isFalse();
    }
}
