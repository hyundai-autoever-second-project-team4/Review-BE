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

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, oAuth2User.getAttributes());

        // Create JWT Tokens
        Map<String, Object> claims = Map.of(
                "email", userInfo.email(),
                "name", userInfo.name(),
                "profileImage", userInfo.profileImage(),
                "social", userInfo.social(),
                "role", userInfo.role()
        );

        String accessToken = jwtTokenProvider.generateAccessToken(claims);
        String refreshToken = jwtTokenProvider.createRefreshToken(userInfo.email());

        // Set Tokens as Cookies
        addCookie(response, "accessToken", accessToken, request.getServerName());
        addCookie(response, "refreshToken", refreshToken, request.getServerName());

        // Redirect user to frontend
        response.sendRedirect(redirectUrl);
    }

    private void addCookie(HttpServletResponse response, String name, String value, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setDomain(domain.contains("localhost") ? "localhost" : "https://theaterup.site");
        cookie.setSecure(!domain.contains("localhost")); // Secure only in production
        cookie.setMaxAge(name.equals("refreshToken") ? 7 * 24 * 60 * 60 : 24 * 60 * 60); // Refresh: 7 days, Access: 1 day
        response.addCookie(cookie);
    }
}
