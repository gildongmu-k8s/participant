package gildongmu.participant.client;

import gildongmu.participant.dto.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userClient")
public interface UserClient {
    @GetMapping(value = "/users/me")
    User getUserInfoFromToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);

    @GetMapping(value = "/users/{id}")
    User getUserInfoFromId(@PathVariable Long id);
}
