package cn.dpc.abtesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class Experiment {

    public Experiment() {

    }
    public Experiment(ExperimentId experimentId, Buckets buckets) {
        this.experimentId = experimentId;
        this.buckets = buckets;
    }

    public Experiment experiment;

    private ExperimentId experimentId;

    private CustomerCriteriaConditionRef customerCriteriaConditionRef;
    private Buckets buckets;
    private ExperimentAssignments assignments;
    private CustomerCriteriaResults customerCriteriaResults;

    private String description;

    public interface Buckets {
        Flux<Bucket> findAll();

        Mono<Bucket> getByKey(String key);

        Mono<Bucket> assignByCustomerId(String customerId);
    }

    @AllArgsConstructor
    @Getter
    public static class ExperimentId {
        private String id;
    }

    public Mono<Assignment> assign(String customerId) {
        return this.assignments.findByCustomerId(customerId)
                .switchIfEmpty(this.customerCriteriaResults.checkAssess(customerId)
                        .flatMap(result -> result.isAccess()
                                ? this.buckets.assignByCustomerId(customerId)
                                .map(bucket -> new Assignment(new Assignment.AssignmentId(this.experimentId.getId(), customerId),
                                        bucket.getKey(),
                                        bucket.getConfig()))
                                : Mono.just(new Assignment(new Assignment.AssignmentId(this.experimentId.getId(), customerId),
                                null,
                                null))
                        ).flatMap(assignments::add)
                );
    }

    @AllArgsConstructor
    @Getter
    public static class Bucket {
        String key;
        String name;
        String config;
    }
}
