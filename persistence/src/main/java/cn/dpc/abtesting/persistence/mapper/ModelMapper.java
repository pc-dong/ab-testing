package cn.dpc.abtesting.persistence.mapper;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ModelMapper {
    Mono<Experiment> findExperimentById(String id);

    Flux<Assignment> findAssignmentsByExperimentId(String experimentId);

    Mono<Assignment> findAssignmentsByExperimentIdAndCustomerId(String experimentId, String customerId);

    Flux<Experiment> findAllExperiments();

    Mono<Experiment> addExperiment(Experiment experiment);

    Mono<Experiment> updateExperiment(Experiment experiment);

    Mono<Object> deleteExperiment(String id);

    Mono<Assignment> addAssignment(Assignment assignment);
}
