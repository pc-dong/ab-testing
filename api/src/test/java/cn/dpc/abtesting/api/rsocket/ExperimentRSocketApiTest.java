package cn.dpc.abtesting.api.rsocket;

import cn.dpc.abtesting.api.dto.ExperimentDto;
import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import cn.dpc.abtesting.persistence.associations.Buckets;
import cn.dpc.abtesting.persistence.associations.CustomerCriteriaConditionRef;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

class ExperimentRSocketApiTest extends RSocketBaseTest{
    @MockBean
    Experiments experiments;

    @Test
    void add() {
        ExperimentDto request = new ExperimentDto();
        request.setBuckets(new ArrayList<>());
        request.setDescription("测试实验");
        CustomerCriteriaCondition customerCriteriaCondition = new CustomerCriteriaCondition();
        customerCriteriaCondition.setSegmentIds(Arrays.asList("111", "222"));
        request.setCustomerCriteriaCondition(customerCriteriaCondition);

        Mockito.when(experiments.add(any(Experiment.class))).thenAnswer(invocation -> {
            Experiment experiment = invocation.getArgument(0, Experiment.class);
            experiment.setId(new Experiment.ExperimentId(UUID.randomUUID().toString()));
            return Mono.just(experiment);
        });

        requester.route("experiments.post" )
                .data(Mono.just(request), ExperimentDto.class)
                .retrieveMono(ExperimentDto.class)
                .as(StepVerifier::create)
                .expectNextMatches(result ->
                    result.getId() != null
                 ).verifyComplete();
    }

    @Test
    void findAll() {
        Mockito.when(experiments.findAll()).thenReturn(Flux.just(genNewExperiment(), genNewExperiment()));

        requester.route("experiments.get" )
                .retrieveFlux(ExperimentDto.class)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {

        Mockito.when(experiments.findById(any(Experiment.ExperimentId.class))).thenReturn(Mono.just(genNewExperiment()));

        requester.route("experiments." + "id" + ".get" )
                .retrieveMono(ExperimentDto.class)
                .as(StepVerifier::create)
                .expectNextMatches(result -> StringUtils.hasLength(result.getId()))
                .verifyComplete();
    }

    @Test
    void update() {
        ExperimentDto request = new ExperimentDto();
        request.setBuckets(new ArrayList<>());
        request.setDescription("测试实验");
        CustomerCriteriaCondition customerCriteriaCondition = new CustomerCriteriaCondition();
        customerCriteriaCondition.setSegmentIds(Arrays.asList("111", "222"));
        request.setCustomerCriteriaCondition(customerCriteriaCondition);

        Mockito.when(experiments.update(any(Experiment.ExperimentId.class), any(Experiment.class)))
                .thenReturn(Mono.just(genNewExperiment()));

        requester.route("experiments." + "id" + ".put" )
                .data(Mono.just(request), ExperimentDto.class)
                .retrieveMono(ExperimentDto.class)
                .as(StepVerifier::create)
                .expectNextMatches(result -> StringUtils.hasLength(result.getId()))
                .verifyComplete();
    }

    @Test
    void delete() {
        Mockito.when(experiments.delete(any(Experiment.ExperimentId.class))).thenReturn(Mono.empty());

        requester.route("experiments." + "id" + ".delete" )
                .retrieveMono(Void.class)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private Experiment genNewExperiment() {
        Experiment experiment = new Experiment(new Experiment.ExperimentId(UUID.randomUUID().toString()), new Buckets(new ArrayList<>()));
        experiment.setCustomerCriteriaConditionRef(new CustomerCriteriaConditionRef(new CustomerCriteriaCondition()));
        return experiment;
    }
}