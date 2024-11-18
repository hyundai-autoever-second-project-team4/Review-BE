package hyundai.movie_review.alarm.controller;

import hyundai.movie_review.alarm.service.AlarmService;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    // 클라이언트 구독
    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return alarmService.subscribe(memberId);
    }


}
