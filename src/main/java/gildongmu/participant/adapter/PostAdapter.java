package gildongmu.participant.adapter;

import gildongmu.participant.client.PostClient;
import gildongmu.participant.dto.transfer.Post;
import gildongmu.participant.dto.transfer.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostAdapter {
    private final PostClient postClient;

    public Post getPost(Long id){
        return postClient.getPost(id);
    }

    public void updatePostStatus(Long id, PostStatus status){

    }
}
