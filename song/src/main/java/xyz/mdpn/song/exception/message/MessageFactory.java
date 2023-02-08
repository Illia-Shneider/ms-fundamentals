package xyz.mdpn.song.exception.message;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageFactory {
    @Value("${spring.profiles.active}")
    private String profile;

    public Message createMessage() {

        if (profile.equals("dev")) {
            return new AdvancedErrorMessage();
        }

        return new ErrorMessage();
    }
}
