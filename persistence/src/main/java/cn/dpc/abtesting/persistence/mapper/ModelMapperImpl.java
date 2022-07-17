package cn.dpc.abtesting.persistence.mapper;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.ExperimentNotExistException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ModelMapperImpl implements ModelMapper {
    private static final ConcurrentHashMap<String, Experiment> experiments = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Assignment>> assignments = new ConcurrentHashMap();

    @Override
    public Mono<Experiment> findExperimentById(String experimentId) {
        return Mono.just(experiments.get(experimentId));
    }

    @Override
    public Flux<Experiment> findAllExperiments() {
        return Flux.fromIterable(experiments.values());
    }

    @Override
    public Mono<Experiment> addExperiment(Experiment experiment) {
        if (null == experiment.getExperimentId()) {
            experiment.setExperimentId(new Experiment.ExperimentId(UUID.randomUUID().toString()));
        }

        experiments.put(experiment.getExperimentId().getId(), experiment);
        return Mono.just(experiment);
    }

    @Override
    public Mono<Experiment> updateExperiment(Experiment experiment) {
        experiments.put(experiment.getExperimentId().getId(), experiment);
        return Mono.just(experiment);
    }

    @Override
    public Mono<Object> deleteExperiment(String experimentId) {
        Object experiment = experiments.get(experimentId);
        if (null == experiment) {
            return Mono.error(new ExperimentNotExistException());
        }

        experiments.remove(experimentId);

        return Mono.just(experiment);
    }

    @Override
    public Mono<Assignment> addAssignment(Assignment assignment) {
        String experimentId = assignment.getId().getExperimentId();
        String customerId = assignment.getId().getCustomerId();
        ConcurrentHashMap<String, Assignment> customerMap = assignments.getOrDefault(experimentId, new ConcurrentHashMap<>());
        customerMap.put(customerId, assignment);
        assignments.put(assignment.getId().getExperimentId(), customerMap);
        return Mono.just(assignment);
    }

    @Override
    public Flux<Assignment> findAssignmentsByExperimentId(String experimentId) {
        return Flux.fromIterable(assignments.get(experimentId).values());
    }

    @Override
    public Mono<Assignment> findAssignmentsByExperimentIdAndCustomerId(String experimentId, String customerId) {
        return Optional.ofNullable(assignments.get(experimentId)).map(map -> Mono.just(map.get(customerId)))
                .orElse(Mono.empty());
    }
}
