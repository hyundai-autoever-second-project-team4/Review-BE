package hyundai.movie_review.comment.controller;

import hyundai.movie_review.comment.dto.CommentCreateRequest;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(
            @RequestBody CommentCreateRequest request
    ) {
        // 1) 서비스에 해당 request 넘겨주기
        Comment comment = commentService.createComment(request);

        // 2) 서비스에서 받은 respones를 프론트에 넘겨주기
        return ResponseEntity.ok(comment);

    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}
