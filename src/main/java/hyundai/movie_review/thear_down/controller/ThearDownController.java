package hyundai.movie_review.thear_down.controller;

import hyundai.movie_review.thear_down.dto.ThearDownResponse;
import hyundai.movie_review.thear_down.service.ThearDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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
