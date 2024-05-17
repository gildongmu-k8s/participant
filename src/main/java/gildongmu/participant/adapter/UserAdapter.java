package gildongmu.participant.adapter;


import gildongmu.participant.client.UserClient;
import gildongmu.participant.dto.transfer.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdapter {
    private final UserClient userClient;

    public User getUserInfoFromToken(String token) {
        return userClient.getUserInfoFromToken(token);
    }

    public User getUserInfoFromId(Long id) {
        return userClient.getUserInfoFromId(id);
    }
}
