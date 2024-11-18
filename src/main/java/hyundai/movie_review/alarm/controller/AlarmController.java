package hyundai.movie_review.alarm.controller;

import hyundai.movie_review.alarm.dto.AlarmReadResponse;
import hyundai.movie_review.alarm.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
@Tag(name = "Alarm", description = "알람 관련 API")
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(
            summary = "알람 구독",
            description = "특정 회원 ID를 기준으로 SSE를 통해 알람을 구독합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "SSE 연결 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
                    @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음", content = @Content)
            }
    )
    @GetMapping(value = "/subscribe/{memberId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return alarmService.subscribe(memberId);
    }

    @Operation(
            summary = "알람 읽기",
            description = "알람 ID를 기준으로 알람을 읽음 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "알람 읽기 성공", content = @Content(schema = @Schema(implementation = AlarmReadResponse.class))),
                    @ApiResponse(responseCode = "404", description = "알람을 찾을 수 없음", content = @Content)
            }
    )
    @PostMapping("/{alarmId}")
    public ResponseEntity<?> readAlarm(@PathVariable long alarmId) {
        alarmService.read(alarmId);

        return ResponseEntity.ok(AlarmReadResponse.of("알람 읽기 처리가 완료되었습니다."));
    }
}
