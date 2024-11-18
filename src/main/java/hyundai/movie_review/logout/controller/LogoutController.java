package hyundai.movie_review.logout.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "로그아웃 관련 API")
public class LogoutController {
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Access Token 삭제
        invalidateCookie(response, "accessToken", request.getServerName());

        // Refresh Token 삭제
        invalidateCookie(response, "refreshToken", request.getServerName());
    }

    private void invalidateCookie(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, null); // 쿠키 값을 null로 설정
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain(domain.contains("localhost") ? "localhost" : "theaterup.site");
        cookie.setSecure(!domain.contains("localhost")); // Secure 속성은 배포 환경에서만 활성화
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }
}
