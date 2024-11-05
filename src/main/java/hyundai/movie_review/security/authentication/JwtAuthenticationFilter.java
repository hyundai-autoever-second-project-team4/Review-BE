package hyundai.movie_review.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.member.dto.MemberAuthenticationDto;
import hyundai.movie_review.security.exception.CustomJwtException;
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
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Authorization 헤더에서 accessToken 찾기
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        // Authorization 헤더가 "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 부분을 accessToken으로 추출
            accessToken = authorizationHeader.substring(7);
        }

        // 2. accessToken이 존재할 때 JWT 검증
        if (accessToken != null) {
            try {
                // JWT 검증 및 클레임 추출
                Map<String, Object> claims = jwtTokenProvider.validateToken(accessToken);

                MemberAuthenticationDto memberAuthenticationDto = new MemberAuthenticationDto(
                        claims);

                // 인증 토큰 생성 및 SecurityContext 설정
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberAuthenticationDto, "", memberAuthenticationDto.getAuthorities());

                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                log.info("Security Context에 유저 객체 생성 성공!");
            } catch (CustomJwtException e) {
                // JWT 검증 실패 시 CustomJwtException 예외 처리
                log.error("JWT 검증 오류: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage(),
                        e.getClass().getSimpleName());
                return;  // 필터 체인을 중단하고 응답을 반환
            } catch (Exception e) {
                // 기타 예외 처리
                log.error("기타 오류 발생: {}", e.getMessage());
                setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred", e.getClass().getSimpleName());
                return;
            }
        } else {
            log.info("Authorization 헤더에 accessToken이 없습니다.");
            log.info("request header : {}", authorizationHeader);
        }

        // 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message,
            String exception) throws IOException {
        // BusinessExceptionResponse 생성
        BusinessExceptionResponse exceptionResponse = new BusinessExceptionResponse(status, message,
                exception);

        // JSON 응답 작성
        response.setStatus(status.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }
}
