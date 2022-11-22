package cn.dpc.abtesting.api.rsocket;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AssignmentRSocketApiTest extends RSocketBaseTest{
    @MockBean(name = "experimentsForClient")
    public Experiments experiments;

    @Mock
    Experiment experiment;

    @BeforeEach
    public void initMockData() {
        when(experiments.findById(any())).thenReturn(Mono.just(experiment));
    }


    @Test
    void getAssignment() {
        when(experiment.assign(anyString())).thenReturn(Mono.just(new Assignment(
                new Assignment.AssignmentId("experimentId", "customerId"),
                "bucketKey", "config")));

        requester.route(String.format("experiments.%s.assignments.%s.get", "experimentId", "customerId"))
                .retrieveMono(Assignment.class)
                .as(StepVerifier::create)
                .expectNextMatches(result -> result.getBucketKey().get().equals("bucketKey"))
                .verifyComplete();
    }
}