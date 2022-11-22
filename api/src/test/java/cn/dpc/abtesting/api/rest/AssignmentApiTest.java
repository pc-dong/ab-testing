package cn.dpc.abtesting.api.rest;

import cn.dpc.abtesting.api.TestConfiguration;
import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AssignmentApi.class)
@ContextConfiguration(classes = TestConfiguration.class)
class AssignmentApiTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean(name = "experimentsForClient")
    public Experiments experiments;

    @Mock
    Experiment experiment;

    @BeforeEach
    public void init() {
        when(experiments.findById(any())).thenReturn(Mono.just(experiment));
    }


    @Test
    void getAssignment() {
        when(experiment.assign(anyString())).thenReturn(Mono.just(new Assignment(
                new Assignment.AssignmentId("experimentId", "customerId"),
                "bucketKey", "config")));

        webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.pathSegment("experiments", "{experimentId}", "assignments", "customerId")
                            .build("experimentId", "customerId"))
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.bucketKey").isEqualTo("bucketKey");
        }
}
