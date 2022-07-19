package cn.dpc.abtesting.api.dto;

import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.persistence.associations.Buckets;
import cn.dpc.abtesting.persistence.associations.CustomerCriteriaConditionRef;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
public class ExperimentDto {
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

    public static Mono<ExperimentDto> fromExperiment(Experiment experiment) {
        return Mono.just(experiment)
                .flatMap(exp ->
                        exp
                                .getBuckets().findAll()
                                .collectList()
                                .map(bucketList -> {
                                    ExperimentDto experimentDto = new ExperimentDto();
                                    experimentDto.setId(exp.getExperimentId().getId());
                                    experimentDto.setDescription(exp.getDescription());
                                    experimentDto.setBuckets(bucketList);
                                    experimentDto.setCustomerCriteriaCondition(exp.getCustomerCriteriaConditionRef().getCondition().orElse(null));
                                    return experimentDto;
                                })
                );
    }
}
