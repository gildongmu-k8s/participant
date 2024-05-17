package gildongmu.participant.dto.response;


import gildongmu.participant.domain.room.entity.Room;
import lombok.Builder;


@Builder
public record RoomResponse(
        Long id,
        String lastChatMessage,
        Integer headCount,
        String thumbnail,
        String title
) {
    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .headCount(room.getHeadcount())
                .thumbnail(room.getPost().getThumbnail().getUrl())
                .title(room.getPost().getTitle())
                .build();
    }
}
