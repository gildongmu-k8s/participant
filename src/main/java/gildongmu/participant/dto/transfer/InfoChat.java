package gildongmu.participant.dto.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoChat {
    private Long roomId;
    private String userNickname;
}
