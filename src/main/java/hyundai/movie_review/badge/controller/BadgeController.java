package hyundai.movie_review.badge.controller;

import hyundai.movie_review.badge.dto.BadgeCountListDto;
import hyundai.movie_review.badge.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @Operation(summary = "뱃지에 대한 유저 카운트", description = "각 뱃지마다 획득한 유저가 몇명이 있는지 조회")
    @ApiResponse(responseCode = "200", description = "뱃지 획득 유저 수 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BadgeCountListDto.class)))
    @GetMapping("/badges")
    public ResponseEntity<?> getBadgeCounts(){
        BadgeCountListDto response = badgeService.getBadgeCountList();

        return ResponseEntity.ok(response);
    }
}
