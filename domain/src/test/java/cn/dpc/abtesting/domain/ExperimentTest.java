package cn.dpc.abtesting.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExperimentTest {

    @Mock
    ExperimentAssignments assignments;

    @Mock
    Experiment.Buckets buckets;

    @Mock
    CustomerCriteriaResults customerCriteriaResults;

    Experiment experiment;

    @BeforeEach
    public void init() {
        experiment = new Experiment();
        experiment.setExperimentId(new Experiment.ExperimentId("111"));
        experiment.setAssignments(assignments);
        experiment.setCustomerCriteriaResults(customerCriteriaResults);
        experiment.setBuckets(buckets);
    }

    @Test
    void assign() {
        when(assignments.findByCustomerId(anyString()))
                .thenReturn(Mono.just(new Assignment(new Assignment.AssignmentId("111", "111"), null, null)));

        when(customerCriteriaResults.checkAssess(anyString())).thenReturn(Mono.just(new CustomerCriteriaResult("1111", true)));

        StepVerifier.create(experiment.assign("11111"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
