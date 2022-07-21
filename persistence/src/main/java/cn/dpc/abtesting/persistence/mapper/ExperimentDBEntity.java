package cn.dpc.abtesting.persistence.mapper;

import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.persistence.associations.Buckets;
import cn.dpc.abtesting.persistence.associations.CustomerCriteriaConditionRef;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
public class ExperimentDBEntity {
    private String id;

    private String description;

    private List<Experiment.Bucket> buckets;

    private CustomerCriteriaCondition customerCriteriaCondition;

    public Experiment toExperiment() {
        Experiment experiment = new Experiment();
        experiment.setBuckets(new Buckets(buckets));
        experiment.setCustomerCriteriaConditionRef(new CustomerCriteriaConditionRef(customerCriteriaCondition));
        experiment.setDescription(description);
        return experiment;
    }

    public static Mono<ExperimentDBEntity> fromExperiment(Experiment experiment) {
        return Mono.just(experiment)
                .flatMap(exp ->
                        exp
                                .getBuckets().findAll()
                                .collectList()
                                .map(bucketList -> {
                                    ExperimentDBEntity experimentDB = new ExperimentDBEntity();
                                    experimentDB.setId(exp.getExperimentId().getId());
                                    experimentDB.setDescription(exp.getDescription());
                                    experimentDB.setBuckets(bucketList);
                                    experimentDB.setCustomerCriteriaCondition(exp.getCustomerCriteriaConditionRef().getCondition().orElse(null));
                                    return experimentDB;
                                })
                );
    }
}
