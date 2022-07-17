package cn.dpc.abtesting.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Experiments {
    Mono<Experiment> findById(Experiment.ExperimentId id);

    Flux<Experiment> findAll();

    Mono<Experiment> add(Experiment experiment);

    Mono<Experiment> update(Experiment experiment);

    Mono<Object> delete(Experiment.ExperimentId experimentId);
}
