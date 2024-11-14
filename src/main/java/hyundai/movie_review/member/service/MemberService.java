package hyundai.movie_review.member.service;

import hyundai.movie_review.member.dto.GetMemberMyPageResponse;
import hyundai.movie_review.member.dto.MemberInfoResponse;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.security.MemberResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberResolver memberResolver;

    public MemberInfoResponse getMemberInfo() {
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        return MemberInfoResponse.of(currentMember);
    }

    public GetMemberMyPageResponse getMemberMyPageInfo(){
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        // 2)
        return GetMemberMyPageResponse.of(currentMember);
    }
}
