package gildongmu.participant.domain.room.entity;

import gildongmu.participant.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rooms")
public class Room extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int headcount;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    public Room(Integer headcount, Long postId, LocalDate startDate, LocalDate endDate) {
        this.headcount = headcount;
        this.postId = postId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void plusHeadCount() {
        headcount++;
    }

    public void minusHeadCount() {
        headcount--;
    }
}
