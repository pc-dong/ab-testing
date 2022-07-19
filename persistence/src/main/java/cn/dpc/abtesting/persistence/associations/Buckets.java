package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Experiment;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RequiredArgsConstructor
public class Buckets implements Experiment.Buckets{
    private final List<Experiment.Bucket> buckets;

    @Override
    public Flux<Experiment.Bucket> findAll() {
        return Flux.fromIterable(buckets);
    }

    @Override
    public Mono<Experiment.Bucket> getByKey(String key) {
        return Mono.justOrEmpty(buckets.stream().filter(bucket -> key.equals(bucket.getKey())).findFirst());
    }

    @Override
    public Mono<Experiment.Bucket> assignByCustomerId(String customerId) {
        if(buckets.isEmpty()) {
            return Mono.empty();
        }

        int index = customerId.hashCode() / buckets.size();
        return Mono.just(buckets.get(index));
    }
}
