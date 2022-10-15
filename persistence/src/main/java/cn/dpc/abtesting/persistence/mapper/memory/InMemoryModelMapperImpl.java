package cn.dpc.abtesting.persistence.mapper.memory;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.ExperimentNotExistException;
import cn.dpc.abtesting.persistence.associations.ExperimentAssignments;
import cn.dpc.abtesting.persistence.associations.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryModelMapperImpl implements ModelMapper {
    private static final ConcurrentHashMap<String, ExperimentDBEntity> experiments = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Assignment>> assignments = new ConcurrentHashMap();

    @Override
    public Mono<Experiment> findExperimentById(String experimentId) {
        return Mono.justOrEmpty(experiments.get(experimentId))
                .map(ExperimentDBEntity::toExperiment)
                .map(experiment -> {
                    experiment.setAssignments(new ExperimentAssignments(this, new Experiment.ExperimentId(experimentId)));
                    return experiment;
                });
    }

    @Override
    public Flux<Experiment> findAllExperiments() {
        return Flux.fromIterable(experiments.values())
                .map(ExperimentDBEntity::toExperiment)
                .map(experiment -> {
                    experiment.setAssignments(new ExperimentAssignments(this, experiment.getId()));
                    return experiment;
                });
    }

    @Override
    public Mono<Experiment> addExperiment(Experiment experiment) {
        if (null == experiment.getId()) {
            experiment.setId(new Experiment.ExperimentId(UUID.randomUUID().toString()));
        }
        return ExperimentDBEntity.fromExperiment(experiment)
                .map(experimentDBEntity -> {
                    experiments.put(experiment.getId().getId(), experimentDBEntity);
                    return experimentDBEntity.toExperiment();
                });
    }

    @Override
    public Mono<Experiment> updateExperiment(String experimentId, Experiment changeExperiment) {
        ExperimentDBEntity dbEntity = experiments.get(experimentId);
        if (null == dbEntity) {
            return Mono.error(new ExperimentNotExistException());
        }

        return ExperimentDBEntity.fromExperiment(changeExperiment)
                .map(changeDBEntity -> {
                    BeanUtils.copyProperties(changeDBEntity, dbEntity);
                    experiments.put(experimentId, dbEntity);
                    return dbEntity.toExperiment();
                });
    }

    @Override
    public Mono<Experiment> deleteExperiment(String experimentId) {
        ExperimentDBEntity dbEntity = experiments.get(experimentId);
        if (null == dbEntity) {
            return Mono.error(new ExperimentNotExistException());
        }

        experiments.remove(experimentId);
        return Mono.just(dbEntity.toExperiment());
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
