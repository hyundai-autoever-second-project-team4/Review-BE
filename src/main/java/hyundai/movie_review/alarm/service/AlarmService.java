package hyundai.movie_review.alarm.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final ConcurrentHashMap<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    // 사용자 구독
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(30_000L); // 타임아웃 설정

        // Emitter를 사용자 ID로 저장
        userEmitters.put(memberId, emitter);

        // 연결 종료/타임아웃 처리
        emitter.onCompletion(() -> userEmitters.remove(memberId));
        emitter.onTimeout(() -> userEmitters.remove(memberId));

        return emitter;
    }

    // 특정 사용자에게 알림 전송
    public void sendNotificationToUser(Long memberId, String message) {
        SseEmitter emitter = userEmitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                userEmitters.remove(memberId); // 전송 실패 시 제거
            }
        }
    }

}
