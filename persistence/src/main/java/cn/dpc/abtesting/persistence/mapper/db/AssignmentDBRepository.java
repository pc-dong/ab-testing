package cn.dpc.abtesting.persistence.mapper.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AssignmentDBRepository extends ReactiveCrudRepository<AssignmentDB, String> {
    Flux<AssignmentDB> findByExperimentId(String experimentId);

    Mono<AssignmentDB> findByExperimentIdAndCustomerId(String experimentId, String customerId);
}
