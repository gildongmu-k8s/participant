package gildongmu.participant.dto.response;


import gildongmu.participant.domain.room.entity.Room;
import lombok.Builder;


@Builder
public record RoomInfoResponse(
        Long id,
        Integer headCount,
        String thumbnail,
        String title
) {
    public static RoomInfoResponse from(Room room) {
        return RoomInfoResponse.builder()
                .id(room.getId())
                .headCount(room.getHeadcount())
                .thumbnail(room.getPost().getThumbnail().getUrl())
                .title(room.getPost().getTitle())
                .build();
    }
}
