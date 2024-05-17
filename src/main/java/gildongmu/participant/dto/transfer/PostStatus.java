package gildongmu.participant.dto.transfer;

public enum PostStatus {
    OPEN("모집 중"),
    CLOSED("모집 완료");

    private final String code;

    PostStatus(final String code) {
        this.code = code;
    }

    public String getCode() { return code; }
}


