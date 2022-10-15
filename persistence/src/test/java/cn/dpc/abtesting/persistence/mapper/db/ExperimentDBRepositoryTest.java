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
public class ExperimentDBRepositoryTest {
    @Autowired
    private ExperimentDBRepository experimentDBRepository;

    @AfterEach
    public void afterEach() {
        experimentDBRepository.deleteAll().block();
    }

    @Test
    public void should_find_all_success() {
        experimentDBRepository.findAll()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_save_success() {
        ExperimentDB experimentDB = new ExperimentDB();
        experimentDB.setId(UUID.randomUUID().toString());
        experimentDB.setDescription("AB实验");

        experimentDBRepository.save(experimentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        experimentDBRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_update_success() {
        String id = UUID.randomUUID().toString();
        ExperimentDB experimentDB = new ExperimentDB();
        experimentDB.setId(id);
        experimentDB.setDescription("AB实验");

        experimentDBRepository.save(experimentDB)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        experimentDBRepository.findById(id)
                .flatMap(item -> {
                    item.setDescription("AB试验2");
                    return experimentDBRepository.save(item);
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        experimentDBRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(item -> item.getDescription().equals("AB试验2"))
                .verifyComplete();
    }
}