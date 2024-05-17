package gildongmu.participant.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import gildongmu.participant.domain.participant.entity.Participant;
import gildongmu.participant.dto.transfer.User;
import lombok.Builder;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipantResponse(
        Long id,
        Boolean isLeader,
        Boolean isAccepted,
        ParticipantUserResponse user
) {
    public static ParticipantResponse from(Participant participant, Long currentUserId) {
        return ParticipantResponse.builder()
                .isLeader(participant.isLeader())
                .isAccepted(participant.isAccepted())
                .id(participant.getId())
                .user(ParticipantUserResponse.from(participant.getUser(), currentUserId))
                .build();
    }

    public static ParticipantResponse from(Participant participant) {
        return ParticipantResponse.builder()
                .id(participant.getId())
                .user(ParticipantUserResponse.from(participant.getUser()))
                .build();
    }

    public static ParticipantResponse of(Long participantId, User user) {
        return ParticipantResponse.builder()
                .id(participantId)
                .user(ParticipantUserResponse.from(user))
                .build();
    }
}
