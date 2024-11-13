package hyundai.movie_review.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.member.dto.MemberAuthenticationDto;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.exception.MemberEmailNotFoundException;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.security.exception.CustomJwtException;
import hyundai.movie_review.security.exception.CustomJwtExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = extractTokenFromCookies(request, "accessToken");
        String refreshToken = extractTokenFromCookies(request, "refreshToken");

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        if (accessToken != null) {
            try {
                Map<String, Object> claims = jwtTokenProvider.validateToken(accessToken);
                authenticateUser(claims);
            } catch (CustomJwtExpiredException e) {
                log.info("accessToken 만료됨. refreshToken을 통해 재발급 시도");

                if (refreshToken == null || refreshToken.trim().isEmpty()) {
                    log.error("RefreshToken이 존재하지 않습니다.");
                    setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Refresh token is missing",
                            "MissingTokenException");
                    return;
                }

                handleRefreshToken(request, response, refreshToken);
            } catch (CustomJwtException e) {
                log.error("JWT 검증 오류: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage(),
                        e.getClass().getSimpleName());
                return;
            } catch (Exception e) {
                log.error("기타 오류 발생: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred", e.getClass().getSimpleName());
                return;
            }
        } else {
            log.info("Authorization 헤더와 쿠키에서 accessToken이 없습니다.");
        }

        filterChain.doFilter(request, response);
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response,
            String refreshToken) throws IOException {
        try {
            String memberEmail = jwtTokenProvider.getMemberEmailByRefreshToken(refreshToken);
            Member member = memberRepository.findByEmail(memberEmail)
                    .orElseThrow(MemberEmailNotFoundException::new);

            Map<String, Object> claims = generateClaims(member);
            String newAccessToken = jwtTokenProvider.generateAccessToken(claims);

            authenticateUser(claims);
            addAccessTokenCookie(response, newAccessToken, request);

            log.info("새로운 accessToken 발급 및 Security Context에 인증 정보 저장 완료");

        } catch (Exception ex) {
            log.error("RefreshToken 처리 중 오류 발생: {}", ex.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Failed to refresh access token",
                    ex.getClass().getSimpleName());
        }
    }

    private Map<String, Object> generateClaims(Member member) {
        return Map.of(
                "email", member.getEmail(),
                "name", member.getName(),
                "profileImage", member.getProfileImage(),
                "social", member.getSocial(),
                "role", member.getMemberRoles().stream().findFirst().get().name()
        );
    }

    private void addAccessTokenCookie(HttpServletResponse response, String accessToken,
            HttpServletRequest request) {
        String domain =
                request.getServerName().contains("localhost") ? "localhost" : "theaterup.site";
        boolean isSecure = !domain.equals("localhost");

        String accessTokenCookie = String.format(
                "accessToken=%s; Domain=%s; Path=/; HttpOnly; %s; Max-Age=%d",
                accessToken,
                domain,
                isSecure ? "Secure; SameSite=None" : "", // 배포 환경에서만 보안 속성 추가
                60 * 60 * 24
        );
        response.addHeader("Set-Cookie", accessTokenCookie);
    }

    private void authenticateUser(Map<String, Object> claims) {
        MemberAuthenticationDto memberAuthenticationDto = new MemberAuthenticationDto(claims);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberAuthenticationDto, null, memberAuthenticationDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String extractTokenFromCookies(HttpServletRequest request, String tokenName) {
        if (request.getCookies() != null) {
            Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> tokenName.equals(cookie.getName()))
                    .findFirst();
            return tokenCookie.map(Cookie::getValue).orElse(null);
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message,
            String exception) throws IOException {
        // 응답이 이미 커밋된 경우 메서드 종료
        if (response.isCommitted()) {
            return;
        }

        // BusinessExceptionResponse 생성
        BusinessExceptionResponse exceptionResponse = new BusinessExceptionResponse(status, message, exception);

        // JSON 응답 작성
        response.setStatus(status.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }
}
