package gildongmu.participant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantException extends RuntimeException {
    private final ErrorCode errorCode;
}