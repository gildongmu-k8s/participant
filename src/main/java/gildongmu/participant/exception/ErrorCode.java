package gildongmu.participant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // global
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    REQUEST_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 파라미터를 확인해주세요."),
    // s3
    FILE_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패하였습니다."),
    WRONG_FILE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식의 확장자입니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패하였습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패하였습니다."),
    // participant
    ALREADY_REGISTERED_PARTICIPANT(HttpStatus.BAD_REQUEST, "이미 참여 신청한 유저입니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 참여자가 없습니다."),
    NOT_LEADER_USER(HttpStatus.UNAUTHORIZED, "해당 글 리더 유저가 아닙니다."),
    NOT_PARTICIPANT_USER(HttpStatus.UNAUTHORIZED, "해당 글 참여 유저가 아닙니다."),
    // room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 소통 방이 없습니다."),
    UNAUTHORIZED_PARTICIPANTS(HttpStatus.BAD_REQUEST, "소통 방 권한이 없는 유저입니다."),
    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}

