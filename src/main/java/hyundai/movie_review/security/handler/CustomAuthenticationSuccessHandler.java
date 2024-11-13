package hyundai.movie_review.security.handler;

import hyundai.movie_review.security.model.OAuth2UserInfo;
import hyundai.movie_review.security.authentication.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // application.yaml의 redirect-url 설정값을 주입받음
    @Value("${app.security.redirect-url}")
    private String redirectUrl;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // 1. OAuth2User 객체 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 2. OAuth2UserInfo 객체 생성
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, oAuth2User.getAttributes());

        // 3. JWT 토큰 생성
        Map<String, Object> claims = Map.of(
                "email", userInfo.email(),
                "name", userInfo.name(),
                "profileImage", userInfo.profileImage(),
                "social", userInfo.social(),
                "role", userInfo.role()
        );

        String accessToken = jwtTokenProvider.generateAccessToken(claims);
        String refreshToken = jwtTokenProvider.createRefreshToken(userInfo.email());

        // 현재 서버가 localhost인지 확인하여 개발 환경인지 판단
        String domain = request.getServerName().contains("localhost") ? "localhost" : "http://localhost:5173/";
        boolean isSecure = !domain.equals("localhost"); // 배포 환경에서는 true, 로컬 테스트에서는 false

        // AccessToken 쿠키 설정
        String accessTokenCookie = String.format(
                "accessToken=%s; Domain=%s; Path=/; HttpOnly; %s; Max-Age=%d",
                accessToken,
                domain,
                isSecure ? "Secure; SameSite=None" : "", // 배포 환경에서만 보안 속성 추가
                60 * 60 * 24
        );
        response.addHeader("Set-Cookie", accessTokenCookie);

        // RefreshToken 쿠키 설정
        String refreshTokenCookie = String.format(
                "refreshToken=%s; Domain=%s; Path=/; HttpOnly; %s; Max-Age=%d",
                refreshToken,
                domain,
                isSecure ? "Secure; SameSite=None" : "",
                7 * 24 * 60 * 60
        );
        response.addHeader("Set-Cookie", refreshTokenCookie);

        response.sendRedirect(redirectUrl);
    }
}
