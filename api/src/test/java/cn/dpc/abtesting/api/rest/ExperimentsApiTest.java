package cn.dpc.abtesting.api.rest;

import cn.dpc.abtesting.api.TestConfiguration;
import cn.dpc.abtesting.api.dto.ExperimentDto;
import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import cn.dpc.abtesting.persistence.associations.Buckets;
import cn.dpc.abtesting.persistence.associations.CustomerCriteriaConditionRef;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = ExperimentsApi.class)
@ContextConfiguration(classes = TestConfiguration.class)
class ExperimentsApiTest {

    @MockBean
    Experiments experiments;

    @Autowired
    WebTestClient client;

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

        client.post()
                .uri("/experiments" )
                .body(Mono.just(request), ExperimentDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    void findAll() {
        Mockito.when(experiments.findAll()).thenReturn(Flux.just(genNewExperiment(), genNewExperiment()));

        client.get()
                .uri("/experiments" )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    void findById() {

        Mockito.when(experiments.findById(any(Experiment.ExperimentId.class))).thenReturn(Mono.just(genNewExperiment()));

        client.get()
                .uri("/experiments/" + "id" )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
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

        client.put()
                .uri("/experiments/" + "id" )
                .body(Mono.just(request), ExperimentDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    void delete() {
        Mockito.when(experiments.delete(any(Experiment.ExperimentId.class))).thenReturn(Mono.empty());

        client.delete()
                .uri("/experiments/" + "id" )
                .exchange()
                .expectStatus()
                .isOk();
    }

    private Experiment genNewExperiment() {
        Experiment experiment = new Experiment(new Experiment.ExperimentId(UUID.randomUUID().toString()), new Buckets(new ArrayList<>()));
        experiment.setCustomerCriteriaConditionRef(new CustomerCriteriaConditionRef(new CustomerCriteriaCondition()));
        return experiment;
    }
}
