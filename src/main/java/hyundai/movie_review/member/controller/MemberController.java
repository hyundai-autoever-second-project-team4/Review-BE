package hyundai.movie_review.member.controller;

import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.member.dto.GetMemberMyPageResponse;
import hyundai.movie_review.member.dto.MemberInfoResponse;
import hyundai.movie_review.member.dto.MemberProfileUpdateRequest;
import hyundai.movie_review.member.dto.MemberProfileUpdateResponse;
import hyundai.movie_review.member.service.MemberService;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.review.dto.ReviewInfoPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인 한 사용자 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberInfoResponse.class))),
            @ApiResponse(responseCode = "401", description = "사용자 인증 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/member")
    public ResponseEntity<?> getMemberInfo() {
        MemberInfoResponse response = memberService.getMemberInfo();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인한 사용자 본인의 마이페이지 정보 조회", description = "현재 로그인한 사용자의 마이페이지 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = GetMemberMyPageResponse.class)))
    @GetMapping("/member/mypage")
    public ResponseEntity<?> getMemberMyPageInfo(){
        GetMemberMyPageResponse response = memberService.getMemberMyPageInfo(null);

        return ResponseEntity.ok(response);
    }

    // 다른 사용자의 마이페이지 정보 조회
    @Operation(summary = "유저의 마이페이지 정보 조회", description = "멤버 id에 해당하는 사용자의 마이페이지 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GetMemberMyPageResponse.class)))
    @GetMapping("/member/{memberId}/mypage")
    public ResponseEntity<?> getOtherUserMyPageInfo(
            @PathVariable("memberId") Long memberId
    ){
        GetMemberMyPageResponse response = memberService.getMemberMyPageInfo(memberId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "사용자 정보 수정",
            description = """
                요청 예시:
                <ul>
                <li><b>HTTP 메서드:</b> PUT</li>
                <li><b>URL:</b> /member/update</li>
                <li><b>헤더:</b> Content-Type: multipart/form-data</li>
                <li><b>본문:</b></li>
                <ul>
                    <li>memberName: 영화 팬</li>
                    <li>memberProfileImg: 선택한 이미지 파일</li>
                    <li>primaryBadgeId: 2</li>
                </ul>
                </ul>
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/member/update")
    public ResponseEntity<?> updateMemberProfileInfo(
//            @ModelAttribute MemberProfileUpdateRequest request
            @RequestParam(value = "memberName", required = false) String memberName,
            @RequestParam(value = "memberProfileImg", required = false) MultipartFile memberProfileImg,
            @RequestParam(value = "primaryBadgeId", required = false) Long primaryBadgeId
    ){
//        MemberProfileUpdateResponse response = memberService.updateMemberInfo(request);
        MemberProfileUpdateResponse response = memberService.updateMemberInfo(memberName, memberProfileImg, primaryBadgeId);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "사용자에 대한 리뷰 전체 조회",
            description = "사용자 id에 해당하는 리뷰 상세 내용을 조회해 페이지네이션으로 전달, "
                    +"type에 likes(좋아요순), latest(최신순), ratingHigh(별점높은순), ratingLow(별점낮은순), comments(댓글많은순)를 받는다.")
    @GetMapping("/member/{memberId}/reviews")
    public ResponseEntity<?> getReviewsByMemberId(
            @PathVariable long memberId,
            @Parameter(example = "likes, latest, ratingHigh, ratingLow, comments 중 택1") @RequestParam(defaultValue = "likes", name = "type") String type,
            @RequestParam(defaultValue = "0", name = "page") Integer page
    ) {
        ReviewInfoPageDto response = memberService.getReviewsByMemberId(memberId, type, page);

        return ResponseEntity.ok(response);
    }

    @Operation(description = "사용자 로그아웃 엔드포인트")
    @PostMapping("/member/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Access Token 삭제
        invalidateCookie(response, "accessToken", request.getServerName());

        // Refresh Token 삭제
        invalidateCookie(response, "refreshToken", request.getServerName());
    }

    private void invalidateCookie(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, null); // 쿠키 값을 null로 설정
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setDomain(domain.contains("localhost") ? "localhost" : "theaterup.site");
        cookie.setSecure(!domain.contains("localhost")); // Secure 속성은 배포 환경에서만 활성화
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

}
