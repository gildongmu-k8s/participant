package gildongmu.participant.controller;

import gildongmu.participant.dto.response.ChatGroupByDateResponse;
import gildongmu.participant.dto.response.ParticipantResponse;
import gildongmu.participant.dto.response.RoomInfoResponse;
import gildongmu.participant.dto.response.RoomResponse;
import gildongmu.participant.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;


    @GetMapping("/{roomId}/chats")
    private ResponseEntity<Slice<ChatGroupByDateResponse>> retrieveChats(
            @PathVariable Long roomId, @PageableDefault(page = 0, size = 10) Pageable pageable, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(roomService.retrieveChats(token, roomId, pageable));
    }

    @GetMapping
    private ResponseEntity<Slice<RoomResponse>> retrieveRooms(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                              @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(roomService.retrieveRooms(token, pageable));
    }


    @GetMapping("/{roomId}")
    private ResponseEntity<RoomInfoResponse> retrieveRoom(@PathVariable Long roomId,
                                                          @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(roomService.retrieveRoom(token, roomId));
    }

    @GetMapping("/{roomId}/participants")
    public ResponseEntity<List<ParticipantResponse>> retrieveParticipants(
            @PathVariable Long roomId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(roomService.retrieveParticipantsByRoomId(roomId, token));
    }
}
