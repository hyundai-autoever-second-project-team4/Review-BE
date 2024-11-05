package hyundai.movie_review.config;

import hyundai.movie_review.security.authentication.CustomUserDetailService;
import hyundai.movie_review.security.handler.CustomAuthenticationSuccessHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // STATELESS 인증을 사용하므로, csrf 설정 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // Session 기반 인증 설정 제거
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        // cors 설정
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // 허용할 출처 설정 (모든 출처를 허용하거나 특정 도메인 추가)
                    config.setAllowedOrigins(List.of(
                            "http://localhost:3000"  // 로컬 개발 환경에서의 요청 허용
                            // 배포된 서버의 도메인 추가 예정
                    ));

                    // 허용할 HTTP 메서드 설정
                    config.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

                    // 쿠키와 자격 증명 사용을 허용
                    config.setAllowCredentials(true);

                    // 허용할 헤더 설정 (모든 헤더 허용)
                    config.setAllowedHeaders(Collections.singletonList("*"));

                    // 클라이언트에서 접근 가능한 헤더 설정
                    config.setExposedHeaders(
                            Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

                    // 프리플라이트 요청의 캐싱 시간 설정 (1시간)
                    config.setMaxAge(60 * 60L);

                    return config;
                }));

        // 특정 경로는 필터 적용 제외
        http.authorizeHttpRequests(request ->
                request.requestMatchers(
                                new AntPathRequestMatcher("/**")    // 어떤 요청이든 인증
                        ).permitAll()
                        .anyRequest().authenticated()
        );

        // OAuth2 로그인 설정
        http.oauth2Login(oauth ->
                oauth.userInfoEndpoint(c -> c.userService(userDetailService))
                        .successHandler(authenticationSuccessHandler)
        );

        return http.build();
    }
}
