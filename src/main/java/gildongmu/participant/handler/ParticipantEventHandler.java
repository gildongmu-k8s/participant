package gildongmu.participant.handler;


import gildongmu.participant.adapter.UserAdapter;
import gildongmu.participant.dto.transfer.InfoChat;
import gildongmu.participant.dto.transfer.ParticipantAcceptedEvent;
import gildongmu.participant.dto.transfer.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ParticipantEventHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserAdapter userAdapter;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleParticipantAcceptedEvent(ParticipantAcceptedEvent event) {
        User user = userAdapter.getUserInfoFromId(event.userId());
        kafkaTemplate.send("chat", InfoChat.builder()
                .roomId(event.roomId())
                .userNickname(user.getNickname()).build());
    }
}
