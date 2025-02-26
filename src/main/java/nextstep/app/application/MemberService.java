package nextstep.app.application;

import nextstep.app.domain.MemberRepository;
import nextstep.app.payload.Oauth2MemberSaveDto;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveOauth2Member(Oauth2MemberSaveDto dto) {
        var member = memberRepository.findByEmail(dto.email());
        if (member.isPresent()) {
            return;
        }
        memberRepository.save(dto.toMember());
    }

}
