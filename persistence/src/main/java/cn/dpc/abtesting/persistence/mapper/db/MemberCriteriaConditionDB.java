package cn.dpc.abtesting.persistence.mapper.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "member_criteria_condition")
@Data
public class MemberCriteriaConditionDB {

    @Column("id")
    private String id;

    @Column("experiment_id")
    private String experimentId;

    @Column("segment_id")
    private String segmentId;

    @Version
    private Long version;

    @Id
    public String getId() {
        return id;
    }
}
