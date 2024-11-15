package hyundai.movie_review.member.controller;

import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.member.dto.GetMemberMyPageResponse;
import hyundai.movie_review.member.dto.MemberInfoResponse;
import hyundai.movie_review.member.dto.MemberProfileUpdateRequest;
import hyundai.movie_review.member.dto.MemberProfileUpdateResponse;
import hyundai.movie_review.member.service.MemberService;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "사용자 마이페이지 정보 조회", description = "현재 로그인한 사용자의 마이페이지 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = GetMemberMyPageResponse.class)))
    @GetMapping("/member/mypage")
    public ResponseEntity<?> getMemberMyPageInfo(){
        GetMemberMyPageResponse response = memberService.getMemberMyPageInfo();

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
            @ModelAttribute MemberProfileUpdateRequest request
    ){
        MemberProfileUpdateResponse response = memberService.updateMemberInfo(request);

        return ResponseEntity.ok(response);
    }
}
