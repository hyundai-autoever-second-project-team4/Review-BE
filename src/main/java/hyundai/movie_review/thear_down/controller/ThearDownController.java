package hyundai.movie_review.thear_down.controller;

import hyundai.movie_review.thear_down.dto.ThearDownResponse;
import hyundai.movie_review.thear_down.service.ThearDownService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ThearDown", description = "리뷰 싫어요 (ThearDown) 관련 API")
@RequestMapping("/theardown")
public class ThearDownController {
    private final ThearDownService thearDownService;

    @PostMapping("/{reviewId}")
    public ResponseEntity<ThearDownResponse> toggleThearDown(
            @PathVariable Long reviewId
    ){
        ThearDownResponse response = thearDownService.toggleThearDown(reviewId);
        return ResponseEntity.ok(response);
    }
}
