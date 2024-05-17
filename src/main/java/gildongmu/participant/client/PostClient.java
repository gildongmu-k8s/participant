package gildongmu.participant.client;

import gildongmu.participant.dto.transfer.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "postClient")
public interface PostClient {
    @GetMapping(value = "/posts/{id}")
    Post getPost(@PathVariable Long id);

}
