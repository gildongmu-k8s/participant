package gildongmu.participant.dto.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String nickname;
    private String profilePath;
}
