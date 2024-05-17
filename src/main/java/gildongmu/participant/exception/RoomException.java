package gildongmu.participant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomException extends RuntimeException {
    private final ErrorCode errorCode;
}