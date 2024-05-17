package gildongmu.participant.domain.participant.entity;


import gildongmu.participant.domain.BaseTimeEntity;
import gildongmu.participant.domain.participant.constant.Status;
import gildongmu.participant.domain.room.entity.Room;
import gildongmu.participant.dto.transfer.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean isLeader;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Status status;

    private Long userId;

    private Long postId;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @Transient
    private User user;

    public void delete() {
        this.status = Status.DELETED;
    }

    public void accept() {
        this.status = Status.ACCEPTED;
    }

    public boolean isAccepted() {
        return Status.ACCEPTED.equals(status);
    }

    public void addUser(User user){
        this.user = user;
    }

    @Builder
    public Participant(boolean isLeader, Status status, Long userId, Long postId, Room room) {
        this.isLeader = isLeader;
        this.status = status;
        this.postId = postId;
        this.userId = userId;
        this.room = room;
    }
}
