package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Experiment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Primary
@Qualifier("experiments")
@RequiredArgsConstructor
public class Experiments implements cn.dpc.abtesting.domain.Experiments {
    private final ModelMapper modelMapper;
    private final CustomerSegments customerSegments;

    @Override
    public Mono<Experiment> findById(Experiment.ExperimentId id) {
        return modelMapper.findExperimentById(id.getId())
                .map(experiment -> {
                    experiment.setAssignments(new ExperimentAssignments(modelMapper, experiment.getId()));
                    experiment.setCustomerCriteriaResults(new CustomerCriteriaResults(customerSegments,
                            experiment.getCustomerCriteriaConditionRef()));
                    return experiment;
                });
    }

    @Override
    public Flux<Experiment> findAll() {
        return modelMapper.findAllExperiments();
    }

    @Override
    public Mono<Experiment> add(Experiment experiment) {
        return modelMapper.addExperiment(experiment);
    }

    @Override
    public Mono<Experiment> update(Experiment.ExperimentId experimentId, Experiment experiment) {
        return modelMapper.updateExperiment(experimentId.getId(), experiment);
    }

    @Override
    public Mono<Void> delete(Experiment.ExperimentId experimentId) {
        return modelMapper.deleteExperiment(experimentId.getId())
                .then();
    }
}
