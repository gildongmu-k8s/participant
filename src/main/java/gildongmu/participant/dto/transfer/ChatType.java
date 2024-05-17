package gildongmu.participant.dto.transfer;

public enum ChatType {
    MESSAGE, IMAGE, INFO;

    public boolean isInfoType() {
        return ChatType.INFO.equals(this);
    }
}
