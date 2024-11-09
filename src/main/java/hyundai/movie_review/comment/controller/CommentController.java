package hyundai.movie_review.comment.controller;

import hyundai.movie_review.comment.dto.*;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @Operation(summary = "작성한 댓글 저장 api",
            description = "리뷰에 작성한 댓글을 저장하는 api입니다. 리뷰 id와 댓글 내용을 전달하세요. "
    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰 id입니다.")
//    })
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
    @Operation(summary = "댓글 수정 api",
            description = "수정한 댓글을 저장하는 api입니다. 댓글 id와 댓글 내용을 전달하세요. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "수정하려는 사용자와 작성한 멤버 id가 다릅니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 id입니다.")
    })
    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(
            @RequestBody CommentUpdateRequest request
    ){
        CommentUpdateResponse response = commentService.updateComment(request);
        return ResponseEntity.ok(response);
    }

    //댓글 삭제
    @Operation(summary = "댓글 삭제 api",
            description = "댓글을 삭제하는 api입니다. 댓글 id를 전달하세요. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "삭제하려는 사용자와 작성한 멤버 id가 다릅니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 id입니다.")
    })
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    //리뷰 댓글의 전체 조회
//    @Operation(summary = "리뷰의 전체 댓글 조회 api",
//            description = "특정 리뷰의 전체 댓글을 조회하는 api입니다. 리뷰 id를 전달하세요. 댓글 리스트를 반환합니다."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰 id입니다.")
//    })
//    @GetMapping("{reviewId}/comments")
//    public ResponseEntity<?> getAllComment(
//            @PathVariable Long reviewId
//    ){
//        List<Comment> comments = commentService.getAllComments(reviewId);
//        return ResponseEntity.ok(comments);
//    }

    //댓글 단일 조회
    @Operation(summary = "단일 댓글 조회 api",
            description = "특정 댓글을 조회하는 api입니다. 댓글 id를 전달하세요. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 id입니다.")
    })
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getComment(
            @PathVariable Long commentId
    ){
        CommentGetResponse response = commentService.getComment(commentId);
        return ResponseEntity.ok(response);
    }
}
