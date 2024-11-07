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
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // 4. JWT 토큰을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(10 * 60); // 10분 = 600초

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        // 5. 응답에 쿠키 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        response.sendRedirect(redirectUrl);
    }
}
