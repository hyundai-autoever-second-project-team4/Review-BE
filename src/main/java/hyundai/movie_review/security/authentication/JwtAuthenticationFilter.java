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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
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

        String accessToken = extractTokenFromHeaders(request, "Authorization");
        String refreshToken = extractTokenFromHeaders(request, "Refresh-Token");

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
            log.info("Authorization 헤더에서 accessToken이 없습니다.");
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
            response.setHeader("Authorization", "Bearer " + newAccessToken);

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

    private void authenticateUser(Map<String, Object> claims) {
        MemberAuthenticationDto memberAuthenticationDto = new MemberAuthenticationDto(claims);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberAuthenticationDto, null, memberAuthenticationDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String extractTokenFromHeaders(HttpServletRequest request, String headerName) {
        String headerValue = request.getHeader(headerName);
        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            return headerValue.substring(7); // "Bearer " 제거
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message,
            String exception) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        BusinessExceptionResponse exceptionResponse = new BusinessExceptionResponse(status, message, exception);

        response.setStatus(status.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }
}
