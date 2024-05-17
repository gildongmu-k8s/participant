package gildongmu.participant.dto.transfer;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private PostStatus status;
    private Short numberOfPeople;
}
