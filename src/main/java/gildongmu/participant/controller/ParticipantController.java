package gildongmu.participant.controller;


import gildongmu.participant.domain.participant.constant.Status;
import gildongmu.participant.dto.response.ParticipantResponse;
import gildongmu.participant.service.ParticipantService;
import gildongmu.participant.util.validator.EnumValue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/participants")
    public ResponseEntity<Void> applyForParticipant(
            @PathVariable Long postId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        participantService.applyForParticipant(postId, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/participants")
    public ResponseEntity<Void> exitParticipant(
            @PathVariable Long postId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        participantService.exitParticipant(postId, token);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/participants/{participantId}")
    public ResponseEntity<Void> denyParticipant(
            @PathVariable Long postId,
            @PathVariable Long participantId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        participantService.denyParticipant(postId, participantId, token);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/participants/{participantId}")
    public ResponseEntity<Void> acceptParticipant(
            @PathVariable Long postId,
            @PathVariable Long participantId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        participantService.acceptParticipant(postId, participantId, token);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantResponse>> retrieveParticipants(
            @PathVariable Long postId, @RequestParam @EnumValue(enumClass = Status.class) String status,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(participantService.retrieveParticipants(postId, token, status));
    }
}
