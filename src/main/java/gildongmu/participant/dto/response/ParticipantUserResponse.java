package gildongmu.participant.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import gildongmu.participant.dto.transfer.User;
import lombok.Builder;

import java.util.Objects;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipantUserResponse(
        Long id,
        String nickname,
        String profilePath,
        boolean isCurrentUser
) {
    public static ParticipantUserResponse from(User user, Long currentUserId) {
        return ParticipantUserResponse.builder()
                .nickname(user.getNickname())
                .id(user.getId())
                .profilePath(user.getProfilePath())
                .isCurrentUser(Objects.equals(currentUserId, user.getId()))
                .build();
    }

    public static ParticipantUserResponse from(User user) {
        return ParticipantUserResponse.builder()
                .nickname(user.getNickname())
                .id(user.getId())
                .profilePath(user.getProfilePath())
                .build();
    }
}
