package nextstep.security.utils;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

    public static String getLastPath(final HttpServletRequest request) {
        var uri = request.getRequestURI();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

}
