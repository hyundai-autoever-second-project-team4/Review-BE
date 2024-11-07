package hyundai.movie_review.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.member.dto.MemberAuthenticationDto;
import hyundai.movie_review.security.exception.CustomJwtException;
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
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에서 accessToken과 refreshToken 추출
        String accessToken = extractTokenFromCookies(request, "accessToken");
        String refreshToken = extractTokenFromCookies(request, "refreshToken");

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        if (accessToken != null) {
            try {
                // JWT 검증 및 클레임 추출
                Map<String, Object> claims = jwtTokenProvider.validateToken(accessToken);

                MemberAuthenticationDto memberAuthenticationDto = new MemberAuthenticationDto(claims);

                // 인증 토큰 생성 및 SecurityContext 설정
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberAuthenticationDto, "", memberAuthenticationDto.getAuthorities());

                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                log.info("Security Context에 유저 객체 생성 성공!");
            } catch (CustomJwtException e) {
                // JWT 검증 실패 시 CustomJwtException 예외 처리
                log.error("JWT 검증 오류: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage(), e.getClass().getSimpleName());
                return;
            } catch (Exception e) {
                // 기타 예외 처리
                log.error("기타 오류 발생: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e.getClass().getSimpleName());
                return;
            }
        } else {
            log.info("Authorization 헤더와 쿠키에서 accessToken이 없습니다.");
        }

        // 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
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
        // BusinessExceptionResponse 생성
        BusinessExceptionResponse exceptionResponse = new BusinessExceptionResponse(status, message, exception);

        // JSON 응답 작성
        response.setStatus(status.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }
}
