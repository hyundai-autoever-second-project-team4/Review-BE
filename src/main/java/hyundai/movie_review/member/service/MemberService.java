package hyundai.movie_review.member.service;

import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.genre.repository.GenreRepository;
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
    private final GenreRepository genreRepository;

    public MemberInfoResponse getMemberInfo() {
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        return MemberInfoResponse.of(currentMember);
    }

    public GetMemberMyPageResponse getMemberMyPageInfo(){
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        // 2) 유저가 작성한 리뷰의 영화 장르들 가져오기
        GenreCountListDto genreCountList = genreRepository.getGenreCountByMember(currentMember);

        return GetMemberMyPageResponse.of(currentMember, genreCountList);
    }
}
