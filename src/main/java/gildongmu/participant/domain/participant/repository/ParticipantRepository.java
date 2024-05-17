package gildongmu.participant.domain.participant.repository;

import gildongmu.participant.domain.participant.constant.Status;
import gildongmu.participant.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    boolean existsByUserIdAndPostIdAndStatusIsNot(Long userId, Long postId, Status status);

    Optional<Participant> findByUserIdAndPostId(Long userId, Long postId);

    Optional<Participant> findByUserIdAndPostIdAndStatusIsNot(Long userId, Long postId, Status status);

    List<Participant> findByPostIdAndStatus(Long postId, Status status);

    List<Participant> findByPostIdAndStatusOrPostIdAndUserId(Long postId, Status status, Long postId2, Long userId);

    @Query("select p from Participant p inner join Room r " +
            "where r.id = ?1 and p.status = 'ACCEPTED' ")
    List<Participant> findAcceptedParticipantsByRoomId(Long roomId);
}
