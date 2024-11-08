package hyundai.movie_review.security.authentication;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.security.model.OAuth2UserInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1. 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. registrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 4. userInfo 객체 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);

        // 5. 멤버 조회 및 저장 로직 수행
        Member member = getOrSave(oAuth2UserInfo);

        // 6. member에 대한 권한 조회
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        member.getMemberRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())));

        // default oauth2 user 객체 반환
        return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    }

    private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
                .orElseGet(oAuth2UserInfo::toEntity);

        return memberRepository.save(member);
    }

}
