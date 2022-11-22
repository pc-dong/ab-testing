package cn.dpc.abtesting.api.rsocket;

import cn.dpc.abtesting.api.TestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(properties = {"spring.rsocket.server.port=0", "spring.main.lazy-initialization=true"}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = TestConfiguration.class)
public class RSocketBaseTest {
    @LocalRSocketServerPort
    int port;
    @Autowired
    RSocketRequester.Builder builder;

    @Autowired
    RSocketMessageHandler messageHandler;

    RSocketRequester requester;

    @BeforeEach
    public void init() {
        requester = builder.rsocketStrategies(messageHandler.getRSocketStrategies())
                .tcp("127.0.0.1", port);
    }
}
