package gildongmu.participant.domain.room.repository;


import gildongmu.participant.domain.room.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByPostId(Long postId);

    @Query("select r.id from Room r inner join Participant p " +
            "where p.userId = ?1 and p.status = 'ACCEPTED' " +
            "order by abs(function('DATEDIFF', r.startDate, current_date)) ")
    Slice<Room> findParticipatedRoomByUserId(Long userId, Pageable pageable);

    @Query("select r.id from Room r inner join Participant p " +
            "where p.userId = ?1 and p.status = 'ACCEPTED' ")
    Set<Long> findAllParticipatedRoomIdsByUserId(Long userId);

    @Query("select r from Room r inner join Participant p " +
            "where r.id = ?1 and p.userId = ?2 and p.status = 'ACCEPTED' ")
    Optional<Room> findParticipatedRoomById(Long id, Long userId);

    @Query("select case when exists " +
            "(select 1 from Room r inner join Participant p " +
            "where r.id = ?1 and p.userId = ?2 and p.status = 'ACCEPTED' ) " +
            "then true else false end ")
    boolean existsParticipatedRoomById(Long id, Long userId);
}
