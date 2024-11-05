package hyundai.movie_review.security.authentication;

import hyundai.movie_review.security.exception.CustomJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(Map<String, Object> values) {

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpiration); // AccessToken 만료 시간

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(values)  // claim 설정
                .setIssuedAt(now)   // iat 설정
                .setExpiration(validity)    // 유효시간 설정
                .signWith(key)  // key로 사인
                .compact();
    }

    // RefreshToken 생성 [Email 값은 중복되지 않으므로 email만 포함되도록 구성]
    public String createRefreshToken() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpiration); // RefreshToken 만료 시간

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setIssuedAt(now) // 발행 시간 설정
                .setExpiration(validity) // 만료 시간 설정
                .signWith(key) // 서명 설정
                .compact();
    }

    public Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {
            // token을 검증하기 위한 key 생성
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

            // key를 통해 claim 분석
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJwtException("[ERROR] Malformed Jwt Token"); // 잘못된 형식의 JWT가 전달된 경우
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJwtException("[ERROR] Expired Jwt Token"); // 만료된 JWT가 전달된 경우
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJwtException("[ERROR] Invalid Jwt Token"); // JWT의 클레임이 유효하지 않은 경우
        } catch (JwtException jwtException) {
            throw new CustomJwtException("[ERROR] JWT Error"); // 기타 JWT 관련 오류 발생 시
        } catch (Exception e) {
            throw new CustomJwtException("[ERROR] Internal Error");// 그 외의 예외 발생 시
        }

        return claim;
    }

}
