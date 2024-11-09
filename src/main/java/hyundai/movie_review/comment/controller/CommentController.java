package hyundai.movie_review.comment.controller;

import hyundai.movie_review.comment.dto.*;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<?> createComment(
            @RequestBody CommentCreateRequest request
    ) {
        // 1) 서비스에 해당 request 넘겨주기
        CommentCreateResponse response = commentService.createComment(request);

        // 2) 서비스에서 받은 respones를 프론트에 넘겨주기
        return ResponseEntity.ok(response);

    }

    //댓글 수정
    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(
            @RequestBody CommentUpdateRequest request
    ){
        CommentUpdateResponse response = commentService.updateComment(request);
        return ResponseEntity.ok(response);
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    //리뷰 댓글의 전체 조회
//    @GetMapping("{reviewId}/comments")
//    public ResponseEntity<?> getAllComment(
//            @PathVariable Long reviewId
//    ){
//        List<Comment> comments = commentService.getAllComments(reviewId);
//        return ResponseEntity.ok(comments);
//    }

    //댓글 단일 조회
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getComment(
            @PathVariable Long commentId
    ){
        CommentGetResponse response = commentService.getComment(commentId);
        return ResponseEntity.ok(response);
    }
}
