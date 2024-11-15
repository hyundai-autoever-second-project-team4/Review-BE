package hyundai.movie_review.member.repository;

import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;
import hyundai.movie_review.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    MemberBadgeAndTierDto getTierAndBadgeImgByMemberId(Long id);

    Page<Member> findAllOrderByReviewCount(Pageable pageable);

    Page<Member> findAllOrderByUpCount(Pageable pageable);

    Page<Member> findAllOrderByCommentCount(Pageable pageable);
}
