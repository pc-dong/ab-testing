package cn.dpc.abtesting.persistence.mapper.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentDBRepository extends ReactiveCrudRepository<ExperimentDB, String> {

}
