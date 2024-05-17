package gildongmu.participant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ParticipantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParticipantApplication.class, args);
    }

}
