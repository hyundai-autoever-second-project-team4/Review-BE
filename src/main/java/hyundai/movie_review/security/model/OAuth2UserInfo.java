package hyundai.movie_review.security.model;

import hyundai.movie_review.member.entity.Member;
import java.time.LocalDateTime;
import java.util.Map;

import hyundai.movie_review.tier.entity.Tier;
import hyundai.movie_review.tier.exception.TierIdNotFound;
import hyundai.movie_review.tier.repository.TierRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
public record OAuth2UserInfo(
        String email,
        String name,
        String profileImage,
        String social,
        MemberRole role
) {

    private static final String KAKAO = "kakao";
    private static final String GITHUB = "github";

    private static final String ADDRESS = "@moviereview.site"; // 도메인으로 변경 예정


    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case KAKAO -> ofKakao(attributes);
            case GITHUB -> ofGithub(attributes);
            default -> throw new IllegalStateException("Unexpected value: " + registrationId);
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profileImage((String) profile.get("profile_image_url"))
                .social(KAKAO)
                .role(MemberRole.MEMBER)
                .build();
    }

    private static OAuth2UserInfo ofGithub(Map<String, Object> attributes) {
        String name = (String) attributes.get("login");
        String email = String.format("%s%s", name, ADDRESS); // GitHub는 email이 제공되지 않아 따로 만들어줌

        return OAuth2UserInfo.builder()
                .name(name)
                .email(email)
                .profileImage((String) attributes.get("avatar_url"))
                .social(GITHUB)
                .role(MemberRole.MEMBER)
                .build();
    }

    public Member toEntity() {
        Member member = Member.builder()
                .email(email)
                .name(name)
                .profileImage(profileImage)
                .social(social)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalScore(0L)
                .build();

        member.addRole(MemberRole.MEMBER);

        return member;
    }
}
