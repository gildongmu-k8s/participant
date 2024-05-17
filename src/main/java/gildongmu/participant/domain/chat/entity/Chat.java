package gildongmu.participant.domain.chat.entity;

import gildongmu.participant.domain.BaseTimeDocument;
import gildongmu.participant.domain.chat.constant.ChatType;
import gildongmu.participant.dto.transfer.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chats")
public class Chat extends BaseTimeDocument {
    @Id
    private String id;
    private ChatType type;
    private Long roomId;
    private String content;
    private ChatUser sender;

    @Builder
    public Chat(ChatType type, Long roomId, String content, ChatUser chatUser) {
        this.type = type;
        this.roomId = roomId;
        this.content = content;
        this.sender = chatUser;
    }

    public Chat updateChatUserProfile(User user){
        this.sender = ChatUser.from(user);
        return this;
    }
}
