package hyundai.movie_review.alarm.service;

import hyundai.movie_review.alarm.dto.AlarmResponse;
import hyundai.movie_review.alarm.entity.Alarm;
import hyundai.movie_review.alarm.exception.AlarmIdNotFoundException;
import hyundai.movie_review.alarm.exception.AlarmNotAuthorizedException;
import hyundai.movie_review.alarm.repository.AlarmRepository;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.security.MemberResolver;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final ConcurrentHashMap<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final AlarmRepository alarmRepository;
    private final MemberResolver memberResolver;

    // 사용자 구독
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무제한 타임아웃 설정

        // 사용자 Emitter 저장
        userEmitters.put(memberId, emitter);

        // 연결 종료 또는 타임아웃 처리
        emitter.onCompletion(() -> userEmitters.remove(memberId));
        emitter.onTimeout(() -> userEmitters.remove(memberId));

        // Keep-Alive 메시지 주기적 전송
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("keep-alive")); // 주기적 Keep-Alive 메시지
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 30, TimeUnit.SECONDS); // 30초마다 Keep-Alive 전송

        return emitter;
    }

    // 특정 사용자에게 알림 전송
    public void sendNotificationToUser(Long memberId, Alarm alarm) {
        Alarm savedAlarm = alarmRepository.save(alarm);

        SseEmitter emitter = userEmitters.get(memberId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("alarm")
                        .data(AlarmResponse.of(savedAlarm)));
            } catch (IOException e) {
                userEmitters.remove(memberId); // 전송 실패 시 제거
            }
        }
    }

    public void read(long alarmId) {
        Member currentMember = memberResolver.getCurrentMember();

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmIdNotFoundException::new);

        if (!currentMember.equals(alarm.getMember())) {
            throw new AlarmNotAuthorizedException();
        }

        alarm.readAlarm();

        alarmRepository.save(alarm);
    }

    public void readAll() {
        Member currentMember = memberResolver.getCurrentMember();

        List<Alarm> alarms = alarmRepository.findAllByMemberId(currentMember.getId());

        alarms.forEach(Alarm::readAlarm);

        alarmRepository.saveAll(alarms);
    }

}
