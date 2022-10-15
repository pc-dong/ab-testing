package cn.dpc.abtesting.persistence.mapper.db;


import cn.dpc.abtesting.domain.Experiment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("bucket")
@Data
public class BucketDB {

    @Version
    private long version;
    private String id;

    @Column("experiment_id")
    private String experimentId;

    @Column("bucket_key")
    private String key;

    private String name;

    private String config;

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    public Experiment.Bucket to() {
         return new Experiment.Bucket(this.key, this.name, this.config);
    }

    public static BucketDB from(Experiment.Bucket bucket, String experimentId) {
        BucketDB bucketDB = new BucketDB();
        bucketDB.setExperimentId(experimentId);
        bucketDB.setKey(bucket.getKey());
        bucketDB.setName(bucket.getName());
        bucketDB.setConfig(bucket.getConfig());
        return bucketDB;
    }
}
