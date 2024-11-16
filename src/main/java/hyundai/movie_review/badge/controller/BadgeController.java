package hyundai.movie_review.badge.controller;

import hyundai.movie_review.badge.dto.BadgeCountListDto;
import hyundai.movie_review.badge.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/badges")
    public ResponseEntity<?> getBadgeCounts(){
        BadgeCountListDto response = badgeService.getBadgeCountList();

        return ResponseEntity.ok(response);
    }
}
