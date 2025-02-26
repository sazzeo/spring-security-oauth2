package nextstep.app.payload;

import nextstep.app.domain.Member;

import java.util.Set;

public record Oauth2MemberSaveDto(
        String email,
        String name,
        String imageUrl) {

    public Member toMember() {
        return new Member(
                email,
                null,
                name,
                imageUrl,
                Set.of("USER")
        );
    }
}
