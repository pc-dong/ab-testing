package cn.dpc.abtesting.persistence.mapper.db;


import cn.dpc.abtesting.domain.Assignment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("assignment")
@Data
public class AssignmentDB {

    @Version
    private long version;
    private String id;

    @Column("experiment_id")
    private String experimentId;

    @Column("customer_id")
    private String customerId;

    @Column("bucket_key")
    private String bucketKey;

    @Column("bucket_config")
    private String bucketConfig;

    public static AssignmentDB from(Assignment assignment) {
        AssignmentDB assignmentDB = new AssignmentDB();
        assignmentDB.setBucketConfig(assignment.getConfig().orElse(null));
        assignmentDB.setBucketKey(assignment.getBucketKey().orElse(""));
        assignmentDB.setExperimentId(assignment.getId().getExperimentId());
        assignmentDB.setCustomerId(assignment.getId().getCustomerId());

        return assignmentDB;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    public Assignment to() {
        return new Assignment(new Assignment.AssignmentId(experimentId, customerId), this.bucketKey, bucketConfig);
    }
}
