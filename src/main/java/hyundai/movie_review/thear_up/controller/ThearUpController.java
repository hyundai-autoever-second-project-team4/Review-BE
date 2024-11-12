package hyundai.movie_review.thear_up.controller;

import hyundai.movie_review.thear_up.dto.ThearUpResponse;
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
@RequestMapping("/thearup")
public class ThearUpController {
    private final ThearUpService thearUpService;

    @PostMapping("/{reviewId}")
    public ResponseEntity<ThearUpResponse> toggleThearUp(
            @PathVariable Long reviewId
    ){
        ThearUpResponse response = thearUpService.toggleThearUp(reviewId);

        return ResponseEntity.ok(response);
    }
}
