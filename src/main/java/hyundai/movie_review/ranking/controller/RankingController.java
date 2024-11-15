package hyundai.movie_review.ranking.controller;

import hyundai.movie_review.ranking.dto.MemberRankingInfoDto;
import hyundai.movie_review.ranking.dto.MemberRankingResponse;
import hyundai.movie_review.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "사용자 랭킹 관련 API")
public class RankingController {

    private final RankingService rankingService;

    @Operation(
            summary = "총 점수 기준 사용자 랭킹 조회",
            description = "총 점수 순으로 사용자 랭킹을 페이지 단위로 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRankingResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/ranking/total-score")
    public ResponseEntity<MemberRankingResponse> getMemberRankingByTotalScore(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MemberRankingInfoDto> rankingPage = rankingService.getRankingByTotalScore(page, size);
        return ResponseEntity.ok(MemberRankingResponse.of(rankingPage));
    }

    @Operation(
            summary = "리뷰 개수 기준 사용자 랭킹 조회",
            description = "리뷰 개수 순으로 사용자 랭킹을 페이지 단위로 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRankingResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/ranking/review-count")
    public ResponseEntity<MemberRankingResponse> getRankingByReviewCount(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Page<MemberRankingInfoDto> rankingPage = rankingService.getRankingByReviewCount(page, size);
        return ResponseEntity.ok(MemberRankingResponse.of(rankingPage));
    }

    @Operation(
            summary = "Up 개수 기준 사용자 랭킹 조회",
            description = "받은 Up 수 순으로 사용자 랭킹을 페이지 단위로 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRankingResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/ranking/up-count")
    public ResponseEntity<MemberRankingResponse> getRankingByUpCount(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Page<MemberRankingInfoDto> rankingPage = rankingService.getRankingByUpCount(page, size);
        return ResponseEntity.ok(MemberRankingResponse.of(rankingPage));
    }

    @Operation(
            summary = "댓글 개수 기준 사용자 랭킹 조회",
            description = "댓글 개수 순으로 사용자 랭킹을 페이지 단위로 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRankingResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/ranking/comment-count")
    public ResponseEntity<MemberRankingResponse> getRankingByCommentCount(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Page<MemberRankingInfoDto> rankingPage = rankingService.getRankingByCommentCount(page, size);
        return ResponseEntity.ok(MemberRankingResponse.of(rankingPage));
    }
}
