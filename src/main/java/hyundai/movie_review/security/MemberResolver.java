package hyundai.movie_review.security;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.exception.MemberEmailNotFoundException;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.security.exception.MemberAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberResolver {

    private final MemberRepository memberRepository;

    // POST, PUT, DELETE 시에 로그인 된 사용자를 가져올 때 사용하는 메소드
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // authentication에서 email 추출

        // 인증되지 않은 사용자인 경우 (anonymous) 예외 처리
        if (!authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new MemberAuthenticationException();  // 예외 발생
        }

        return memberRepository.findByEmail(email)
                .orElseThrow(MemberEmailNotFoundException::new);
    }

    // GET 요청 시에 로그인 된 사용자 여부 확인 메소드
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 사용자인 경우 false
        return authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
