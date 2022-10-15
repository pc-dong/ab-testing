package cn.dpc.abtesting.persistence.mapper.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BucketDBRepository extends ReactiveCrudRepository<BucketDB, String> {
    Flux<BucketDB> findByExperimentId(String experimentId);

    Mono<Void> deleteAllByExperimentId(String experimentId);
}
