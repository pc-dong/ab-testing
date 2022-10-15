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
public class BucketDBRepositoryTest {
    @Autowired
    private BucketDBRepository bucketDBRepository;

    @AfterEach
    public void afterEach() {
        bucketDBRepository.deleteAll().block();
    }

    @Test
    public void should_find_all_success() {
        bucketDBRepository.findAll()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_save_success() {
        BucketDB bucketDB = new BucketDB();
        bucketDB.setId(UUID.randomUUID().toString());
        bucketDB.setExperimentId("100000");
        bucketDB.setKey("bucket1");
        bucketDB.setName("bucketName");

        bucketDBRepository.save(bucketDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bucketDBRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_find_by_experiment_id_success() {
        String experimentId = "100000";
        BucketDB bucketDB = new BucketDB();
        bucketDB.setId(UUID.randomUUID().toString());
        bucketDB.setExperimentId(experimentId);
        bucketDB.setKey("bucket1");
        bucketDB.setName("bucketName");

        bucketDBRepository.save(bucketDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bucketDBRepository.findByExperimentId(experimentId)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_update_success() {
        String id = UUID.randomUUID().toString();
        BucketDB bucketDB = new BucketDB();
        bucketDB.setId(id);
        bucketDB.setExperimentId("100000");
        bucketDB.setKey("bucket1");
        bucketDB.setName("bucketName");

        bucketDBRepository.save(bucketDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bucketDBRepository.findById(id)
                .flatMap(item -> {
                    item.setName("bucketName2");
                    return bucketDBRepository.save(item);
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bucketDBRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(item -> item.getName().equals("bucketName2"))
                .verifyComplete();
    }

    @Test
    public void should_delete_all_by_experiment_id_success() {
        String experimentId1 = "100000";
        BucketDB bucketDB = new BucketDB();
        bucketDB.setId(UUID.randomUUID().toString());
        bucketDB.setExperimentId(experimentId1);
        bucketDB.setKey("bucket1");
        bucketDB.setName("bucketName");

        bucketDBRepository.save(bucketDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();


        bucketDBRepository.deleteAllByExperimentId(experimentId1)
                .as(StepVerifier::create)
                .verifyComplete();

        bucketDBRepository.findAll()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}