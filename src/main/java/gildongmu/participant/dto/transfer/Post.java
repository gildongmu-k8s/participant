package gildongmu.participant.dto.transfer;

import lombok.*;

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
    private String title;
    private Image thumbnail;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Image {
        private Long id;
        private String url;
    }
}
