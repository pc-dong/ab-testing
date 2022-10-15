package cn.dpc.abtesting.persistence.mapper.db;

import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.persistence.associations.Buckets;
import cn.dpc.abtesting.persistence.associations.CustomerCriteriaConditionRef;
import cn.dpc.abtesting.persistence.associations.ExperimentAssignments;
import cn.dpc.abtesting.persistence.associations.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
public class DbModelMapperImpl implements ModelMapper {
    private final ExperimentDBRepository experimentDBRepository;
    private final BucketDBRepository bucketDBRepository;

    private final MemberCriteriaConditionDBRepository memberCriteriaConditionDBRepository;

    private final AssignmentDBRepository assignmentDBRepository;

    @Override
    public Mono<Experiment> findExperimentById(String id) {
        return Mono.zip(experimentDBRepository.findById(id), bucketDBRepository.findByExperimentId(id).collectList(),
                        this.memberCriteriaConditionDBRepository.findByExperimentId(id).collectList())
                .map(tuple -> {
                    ExperimentDB experimentDB = tuple.getT1();
                    List<BucketDB> bucketDBList = tuple.getT2();
                    List<MemberCriteriaConditionDB> memberCriteriaConditionDBS = tuple.getT3();
                    Experiment experiment = experimentDB.to();
                    experiment.setBuckets(new Buckets(bucketDBList.stream().map(BucketDB::to).collect(Collectors.toList())));
                    experiment.setCustomerCriteriaConditionRef(new CustomerCriteriaConditionRef(new CustomerCriteriaCondition(memberCriteriaConditionDBS.stream().map(MemberCriteriaConditionDB::getSegmentId).collect(Collectors.toList()))));
                    return experiment;
                })
                .map(experiment -> {
                    experiment.setAssignments(new ExperimentAssignments(this, experiment.getId()));
                    return experiment;
                });
    }

    @Override
    public Flux<Assignment> findAssignmentsByExperimentId(String experimentId) {
        return assignmentDBRepository.findByExperimentId(experimentId)
                .map(AssignmentDB::to);
    }

    @Override
    public Mono<Assignment> findAssignmentsByExperimentIdAndCustomerId(String experimentId, String customerId) {
        return assignmentDBRepository.findByExperimentIdAndCustomerId(experimentId, customerId)
                .map(AssignmentDB::to);
    }

    @Override
    public Flux<Experiment> findAllExperiments() {
        return experimentDBRepository.findAll()
                .map(ExperimentDB::to);
    }

    @Override
    @Transactional
    public Mono<Experiment> addExperiment(Experiment experiment) {
        String experimentId = null == experiment.getId().getId() ? UUID.randomUUID().toString() : experiment.getId().getId();
        ExperimentDB experimentDB = new ExperimentDB();
        experimentDB.setId(experimentId);
        experimentDB.setDescription(experiment.getDescription());
        experiment.setId(new Experiment.ExperimentId(experimentId));

        Mono<List<BucketDB>> bucketsMono = getBucketDBsMono(experimentId, experiment);

        List<MemberCriteriaConditionDB> memberCriteriaConditionDBS = getMemberCriteriaConditionDBS(experimentId, experiment);


        return bucketsMono.flatMap(buckets ->
                        bucketDBRepository.saveAll(buckets).then()
                )
                .then(memberCriteriaConditionDBRepository.saveAll(memberCriteriaConditionDBS).then())
                .then(experimentDBRepository.save(experimentDB))
                .then(Mono.just(experiment));
    }

    @Override
    public Mono<Experiment> updateExperiment(String experimentId, Experiment experiment) {
        experiment.setId(new Experiment.ExperimentId(experimentId));
        Mono<List<BucketDB>> bucketsMono = getBucketDBsMono(experimentId, experiment);

        List<MemberCriteriaConditionDB> memberCriteriaConditionDBS = getMemberCriteriaConditionDBS(experimentId, experiment);


        return experimentDBRepository.findById(experimentId)
                .flatMap(experimentDB -> {
                    experimentDB.setDescription(experiment.getDescription());
                    return experimentDBRepository.save(experimentDB);
                }).then(bucketDBRepository.deleteAllByExperimentId(experiment.getId().getId()))
                .then(bucketsMono.flatMap(buckets ->
                        bucketDBRepository.saveAll(buckets).then()
                ))
                .then(memberCriteriaConditionDBRepository.deleteAllByExperimentId(experiment.getId().getId()))
                .then(memberCriteriaConditionDBRepository.saveAll(memberCriteriaConditionDBS).then())
                .then(Mono.just(experiment));
    }
    @Override
    public Mono<Experiment> deleteExperiment(String id) {
        return this.findExperimentById(id)
                .flatMap(experiment -> bucketDBRepository.deleteAllByExperimentId(id)
                        .then(memberCriteriaConditionDBRepository.deleteAllByExperimentId(id))
                        .then(Mono.just(experiment))
                );
    }

    @Override
    public Mono<Assignment> addAssignment(Assignment assignment) {
        AssignmentDB assignmentDB = AssignmentDB.from(assignment);
        return assignmentDBRepository.save(assignmentDB)
                .map(AssignmentDB::to);
    }

    private static List<MemberCriteriaConditionDB> getMemberCriteriaConditionDBS(String experimentId, Experiment experiment) {
        return experiment.getCustomerCriteriaConditionRef().getCondition()
                .map(item ->
                        item.getSegmentIds().stream().map(segId -> {
                            MemberCriteriaConditionDB memberCriteriaConditionDB = new MemberCriteriaConditionDB();
                            memberCriteriaConditionDB.setExperimentId(experimentId);
                            memberCriteriaConditionDB.setSegmentId(segId);
                            return memberCriteriaConditionDB;
                        }).collect(Collectors.toList())
                ).orElse(new ArrayList<>());
    }

    private static Mono<List<BucketDB>> getBucketDBsMono(String experimentId, Experiment experiment) {
        return experiment.getBuckets().findAll()
                .map(bucket -> BucketDB.from(bucket, experimentId))
                .collectList();
    }
}
