package gildongmu.participant.service;

import gildongmu.participant.adapter.PostAdapter;
import gildongmu.participant.adapter.UserAdapter;
import gildongmu.participant.domain.participant.constant.Status;
import gildongmu.participant.domain.participant.entity.Participant;
import gildongmu.participant.domain.participant.repository.ParticipantRepository;
import gildongmu.participant.domain.room.entity.Room;
import gildongmu.participant.domain.room.repository.RoomRepository;
import gildongmu.participant.dto.response.ParticipantResponse;
import gildongmu.participant.dto.transfer.ParticipantAcceptedEvent;
import gildongmu.participant.dto.transfer.Post;
import gildongmu.participant.dto.transfer.PostStatus;
import gildongmu.participant.dto.transfer.User;
import gildongmu.participant.exception.ErrorCode;
import gildongmu.participant.exception.ParticipantException;
import gildongmu.participant.exception.PostException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserAdapter userAdapter;
    private final PostAdapter postAdapter;

    public void applyForParticipant(Long postId, String token) {
        Post post = postAdapter.getPost(postId);
        User user = userAdapter.getUserInfoFromToken(token);
        if (participantRepository.existsByUserIdAndPostId(user.getId(), postId))
            throw new ParticipantException(ErrorCode.ALREADY_REGISTERED_PARTICIPANT);

        participantRepository.save(Participant.builder()
                .isLeader(false)
                .status(Status.PENDING)
                .userId(user.getId())
                .postId(postId)
                .build());
    }

    @Transactional
    public void exitParticipant(Long postId, String token) {
        User user = userAdapter.getUserInfoFromToken(token);
        Participant participant = participantRepository.findByUserIdAndPostIdAndStatusIsNot(user.getId(), postId, Status.DELETED)
                .orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));

        if (Status.ACCEPTED.equals(participant.getStatus()))
            roomRepository.findByPostId(postId)
                    .ifPresent(Room::minusHeadCount);
        participant.delete();
    }

    public void saveLeader(Post post, User user) {
        participantRepository.save(Participant.builder()
                .isLeader(true)
                .postId(post.getId())
                .status(Status.ACCEPTED)
                .userId(user.getId())
                .build());
    }

    @Transactional
    public void denyParticipant(Long postId, Long participantId, String token) {
        User user = userAdapter.getUserInfoFromToken(token);
        validateLeaderUser(user.getId(), postId);

        Participant participantToBeDeleted = participantRepository.findById(participantId)
                .stream().filter(participant -> Objects.equals(participant.getPostId(), postId))
                .findFirst().orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));

        if (Status.ACCEPTED.equals(participantToBeDeleted.getStatus()))
            roomRepository.findByPostId(postId)
                    .ifPresent(Room::minusHeadCount);
        participantToBeDeleted.delete();

    }

    @Transactional
    public void acceptParticipant(Long postId, Long participantId, String token) {
        Post post = postAdapter.getPost(postId);
        User user = userAdapter.getUserInfoFromToken(token);

        if (!Objects.equals(post.getStatus(), PostStatus.OPEN))
            throw new PostException(ErrorCode.POST_NOT_FOUND);

        if (!Objects.equals(post.getUserId(), user.getId()))
            throw new PostException(ErrorCode.POST_NOT_FOUND);

        Participant participantToBeAccepted = participantRepository.findById(participantId)
                .stream().filter(participant -> Objects.equals(participant.getPostId(), postId)
                        && Objects.equals(participant.getStatus(), Status.PENDING))
                .findFirst().orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));
        participantToBeAccepted.accept();

        handlingParticipantAcceptedEvent(post, participantToBeAccepted.getUserId());
    }

    // TODO: extract to event listener
    public void handlingParticipantAcceptedEvent(Post post, Long participantUserId) {
        Room room = roomRepository.findByPostId(post.getId())
                .orElseGet(() -> roomRepository.save(Room.builder()
                        .postId(post.getId())
                        .headcount(1)
                        .build()));
        room.plusHeadCount();
        if (room.getHeadcount() == post.getNumberOfPeople())
            postAdapter.updatePostStatus(post.getId(), PostStatus.CLOSED);
        applicationEventPublisher.publishEvent(ParticipantAcceptedEvent.builder()
                .roomId(room.getId())
                .userId(participantUserId).build());

    }


    private void validateLeaderUser(Long userId, Long postId) {
        participantRepository.findByUserIdAndPostId(userId, postId)
                .stream().filter(Participant::isLeader).findFirst()
                .orElseThrow(() -> new ParticipantException(ErrorCode.NOT_LEADER_USER));
    }

    public List<ParticipantResponse> retrieveParticipants(Long postId, String token, String status) {
        User user = userAdapter.getUserInfoFromToken(token);
        if (Status.PENDING.name().equals(status))
            return retrievePendingParticipants(postId, user);
        return retrieveAcceptedParticipants(postId, user);
    }


    private List<ParticipantResponse> retrievePendingParticipants(Long postId, User user) {
        validateLeaderUser(user.getId(), postId);
        return participantRepository.findByPostIdAndStatus(postId, Status.PENDING)
                .stream().map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    private List<ParticipantResponse> retrieveAcceptedParticipants(Long postId, User user) {
        validateParticipantUser(user.getId(), postId);
        return participantRepository.findByPostIdAndStatusOrPostIdAndUserId(postId, Status.ACCEPTED, postId, user.getId())
                .stream().map(participant -> ParticipantResponse.from(participant, user.getId()))
                .sorted((o1, o2) -> {
                    if (o1.user().isCurrentUser() == o2.user().isCurrentUser())
                        return Boolean.compare(o2.isLeader(), o1.isLeader());
                    return Boolean.compare(o2.user().isCurrentUser(), o1.user().isCurrentUser());
                })
                .collect(Collectors.toList());
    }

    private void validateParticipantUser(Long userId, Long postId) {
        if (!participantRepository.existsByUserIdAndPostIdAndStatusIsNot(userId, postId, Status.DELETED))
            throw new ParticipantException(ErrorCode.NOT_PARTICIPANT_USER);
    }


}
