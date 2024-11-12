package hyundai.movie_review.thear_up.controller;

import hyundai.movie_review.thear_up.service.ThearUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ThearUpController {
    private final ThearUpService thearUpService;

    @PostMapping("/{reviewId}/thearup")
    public ResponseEntity<?> toggleThearUp(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal Long memberId){
        boolean result = thearUpService.addThearUp(reviewId, memberId);
        return ResponseEntity.ok(result);
    }
}
