package cn.dpc.abtesting.persistence.mapper.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
@Rollback
public class MemberCriteriaConditionDBRepositoryTest {
    @Autowired
    private MemberCriteriaConditionDBRepository repository;

    @AfterEach
    public void afterEach() {
        repository.deleteAll().block();
    }

    @Test
    public void should_find_all_success() {
        repository.findAll()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_save_success() {
        MemberCriteriaConditionDB memberCriteriaConditionDB = new MemberCriteriaConditionDB();
        memberCriteriaConditionDB.setId(UUID.randomUUID().toString());
        memberCriteriaConditionDB.setExperimentId("100000");
        memberCriteriaConditionDB.setSegmentId("segId1");

        repository.save(memberCriteriaConditionDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_id_success() {
        MemberCriteriaConditionDB memberCriteriaConditionDB = new MemberCriteriaConditionDB();
        memberCriteriaConditionDB.setId(UUID.randomUUID().toString());
        String experimentId = "100000";
        memberCriteriaConditionDB.setExperimentId(experimentId);
        memberCriteriaConditionDB.setSegmentId("segId1");

        repository.save(memberCriteriaConditionDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findByExperimentId(experimentId)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_update_success() {
        MemberCriteriaConditionDB memberCriteriaConditionDB = new MemberCriteriaConditionDB();
        String id = UUID.randomUUID().toString();
        memberCriteriaConditionDB.setId(id);
        memberCriteriaConditionDB.setExperimentId("100000");
        memberCriteriaConditionDB.setSegmentId("segId1");

        repository.save(memberCriteriaConditionDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findById(id)
                .flatMap(item -> {
                    item.setSegmentId("bucketName2");
                    return repository.save(item);
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(item -> item.getSegmentId().equals("bucketName2"))
                .verifyComplete();
    }

    @Test
    public void should_delete_by_experiment_id_success() {
        String experimentId = "100000";
        MemberCriteriaConditionDB memberCriteriaConditionDB = new MemberCriteriaConditionDB();
        memberCriteriaConditionDB.setId(UUID.randomUUID().toString());
        memberCriteriaConditionDB.setExperimentId(experimentId);
        memberCriteriaConditionDB.setSegmentId("segId1");

        repository.save(memberCriteriaConditionDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.deleteAllByExperimentId(experimentId)
                .as(StepVerifier::create)
                .verifyComplete();

        repository.findAll()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}