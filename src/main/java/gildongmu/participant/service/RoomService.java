package gildongmu.participant.service;


import gildongmu.participant.adapter.PostAdapter;
import gildongmu.participant.adapter.UserAdapter;
import gildongmu.participant.domain.chat.entity.Chat;
import gildongmu.participant.domain.chat.repository.ChatMongoRepository;
import gildongmu.participant.domain.participant.repository.ParticipantRepository;
import gildongmu.participant.domain.room.entity.Room;
import gildongmu.participant.domain.room.repository.RoomRepository;
import gildongmu.participant.dto.response.*;
import gildongmu.participant.dto.transfer.Post;
import gildongmu.participant.dto.transfer.User;
import gildongmu.participant.exception.ErrorCode;
import gildongmu.participant.exception.RoomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChatMongoRepository chatMongoRepository;
    private final ParticipantRepository participantRepository;
    private final UserAdapter userAdapter;
    private final PostAdapter postAdapter;

    public RoomInfoResponse retrieveRoom(String token, Long roomId) {
        User user = userAdapter.getUserInfoFromToken(token);
        Room room = roomRepository.findParticipatedRoomById(roomId, user.getId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));
        Post post = postAdapter.getPost(room.getId());
        room.addPost(post);
        return RoomInfoResponse.from(room);
    }

    public Slice<RoomResponse> retrieveRooms(String token, Pageable pageable) {
        User user = userAdapter.getUserInfoFromToken(token);
        return roomRepository.findParticipatedRoomByUserId(user.getId(), pageable)
                .map(RoomResponse::from);
    }

    public Slice<ChatGroupByDateResponse> retrieveChats(String token, Long roomId, Pageable pageable) {
        User user = userAdapter.getUserInfoFromToken(token);
        validateRetrieveRoom(roomId, user.getId());

        return getGroupingChatSlices(chatMongoRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable), user.getId());
    }

    public Slice<ChatGroupByDateResponse> getGroupingChatSlices(Slice<Chat> chatSlice, Long userId) {
        if (chatSlice.getContent().isEmpty())
            return new SliceImpl<>(new ArrayList<>(), chatSlice.getPageable(), chatSlice.hasNext());
        List<ChatGroupByDateResponse> content = new ArrayList<>();
        List<ChatResponse> sameDayChats = new ArrayList<>();
        LocalDate prevDate = chatSlice.getContent().get(0).getCreatedAt().toLocalDate();
        for (Chat chat : chatSlice.getContent()) {
            LocalDate curDate = chat.getCreatedAt().toLocalDate();
            if (!prevDate.equals(curDate)) {
                content.add(ChatGroupByDateResponse.of(prevDate, sameDayChats));
                sameDayChats = new ArrayList<>();
                prevDate = curDate;
            }
            sameDayChats.add(ChatResponse.from(chat, userId));
        }
        content.add(ChatGroupByDateResponse.of(prevDate, sameDayChats));
        return new SliceImpl<>(content, chatSlice.getPageable(), chatSlice.hasNext());
    }


    private void validateRetrieveRoom(Long roomId, Long userId) {
        if (!roomRepository.existsParticipatedRoomById(roomId, userId))
            throw new RoomException(ErrorCode.ROOM_NOT_FOUND);
    }


    public List<ParticipantResponse> retrieveParticipantsByRoomId(Long roomId, String token) {
        User user = userAdapter.getUserInfoFromToken(token);
        validateRetrieveRoom(roomId, user.getId());
        return participantRepository.findAcceptedParticipantsByRoomId(roomId)
                .stream().map(participant -> ParticipantResponse.from(participant, user.getId()))
                .sorted((o1, o2) -> {
                    if (o1.isLeader() == o2.isLeader()) {
                        if (o2.user().isCurrentUser() == o1.user().isCurrentUser())
                            return o2.user().nickname().compareTo(o1.user().nickname());
                        return Boolean.compare(o2.user().isCurrentUser(), o1.user().isCurrentUser());
                    }
                    return Boolean.compare(o2.isLeader(), o1.isLeader());
                }).collect(Collectors.toList());
    }

}
