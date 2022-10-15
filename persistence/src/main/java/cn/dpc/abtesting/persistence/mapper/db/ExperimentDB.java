package cn.dpc.abtesting.persistence.mapper.db;


import cn.dpc.abtesting.domain.Experiment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;


@Table("experiment")
@Data
public class ExperimentDB {

    @Version
    private long version;
    private String id;

    private String description;


    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

   public Experiment to() {
        Experiment experiment = new Experiment();
        experiment.setId(new Experiment.ExperimentId(id));
        experiment.setDescription(description);
        return experiment;
   }
}
