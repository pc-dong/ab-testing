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
public class AssignmentDBRepositoryTest {
    @Autowired
    private AssignmentDBRepository repository;

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
        AssignmentDB assignmentDB = new AssignmentDB();
        assignmentDB.setId(UUID.randomUUID().toString());
        assignmentDB.setExperimentId("experimentId1");
        assignmentDB.setCustomerId("customerId");
        assignmentDB.setBucketKey("bucketA");
        assignmentDB.setBucketConfig("config");

        repository.save(assignmentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_update_success() {
        AssignmentDB assignmentDB = new AssignmentDB();
        String id = UUID.randomUUID().toString();
        assignmentDB.setId(id);
        assignmentDB.setExperimentId("experimentId1");
        assignmentDB.setCustomerId("customerId");
        assignmentDB.setBucketKey("bucketA");
        assignmentDB.setBucketConfig("config");

        repository.save(assignmentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findById(id)
                .flatMap(item -> {
                    item.setBucketKey("bucketB");
                    return repository.save(item);
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(item -> item.getBucketKey().equals("bucketB"))
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_return_empty() {
        repository.findByExperimentId("experimentId")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_return_data_when_data_exists() {
        AssignmentDB assignmentDB = new AssignmentDB();
        assignmentDB.setId(UUID.randomUUID().toString());
        String experimentId = "experimentId1";
        assignmentDB.setExperimentId(experimentId);
        assignmentDB.setCustomerId("customerId");
        assignmentDB.setBucketKey("bucketA");
        assignmentDB.setBucketConfig("config");

        repository.save(assignmentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findByExperimentId(experimentId)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_id_and_customer_id_return_empty() {
        String experimentId = "experimentId1";
        String customerId = "customerId";
        repository.findByExperimentIdAndCustomerId(experimentId, customerId)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_id_and_customer_id_return_data() {
        AssignmentDB assignmentDB = new AssignmentDB();
        assignmentDB.setId(UUID.randomUUID().toString());
        String experimentId = "experimentId1";
        assignmentDB.setExperimentId(experimentId);
        String customerId = "customerId";
        assignmentDB.setCustomerId(customerId);
        assignmentDB.setBucketKey("bucketA");
        assignmentDB.setBucketConfig("config");

        repository.save(assignmentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        repository.findByExperimentIdAndCustomerId(experimentId, customerId)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}