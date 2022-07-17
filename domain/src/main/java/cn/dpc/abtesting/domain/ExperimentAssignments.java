package cn.dpc.abtesting.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExperimentAssignments {

    Flux<Assignment> findAll();
    Mono<Assignment> findByCustomerId(String customerId);
    Mono<Assignment> add(Assignment assignment);
}
