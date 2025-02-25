package nextstep.security.properties;

import jakarta.annotation.Nullable;

public interface ClientRegistrationRepository {
    @Nullable
    Registration findRegistrationById(final String id);
}
