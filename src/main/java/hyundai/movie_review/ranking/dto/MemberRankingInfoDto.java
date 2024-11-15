package hyundai.movie_review.ranking.dto;

import hyundai.movie_review.member.entity.Member;

public record MemberRankingInfoDto(
        int rank,
        long memberId,
        String tierImage,
        String profileImage,
        String memberName,
        long totalReviewCount,
        long totalThearUpCount,
        long totalCommentCount,
        long memberTotalScore
        ) {

    public static MemberRankingInfoDto of(Member member, int rank) {
        return new MemberRankingInfoDto(
                rank,
                member.getId(),
                member.getTier().getImage(),
                member.getProfileImage(),
                member.getName(),
                member.getReviews().stream()
                        .filter(review -> !review.getDeleted())
                        .count(),
                member.getThearUps().size(),
                member.getComments().size(),
                member.getTotalScore()
        );
    }
}
