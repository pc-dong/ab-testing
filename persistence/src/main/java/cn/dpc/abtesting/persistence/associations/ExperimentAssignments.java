package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.persistence.mapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ExperimentAssignments implements cn.dpc.abtesting.domain.ExperimentAssignments {
    private final ModelMapper modelMapper;
    private final Experiment.ExperimentId experimentId;

    @Override
    public Flux<Assignment> findAll() {
        return modelMapper.findAssignmentsByExperimentId(experimentId.getId());
    }

    @Override
    public Mono<Assignment> findByCustomerId(String customerId) {
        return modelMapper.findAssignmentsByExperimentIdAndCustomerId(experimentId.getId(), customerId);
    }

    @Override
    public Mono<Assignment> add(Assignment assignment) {
        return modelMapper.addAssignment(assignment);
    }
}
