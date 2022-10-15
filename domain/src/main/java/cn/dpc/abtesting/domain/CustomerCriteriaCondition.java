package cn.dpc.abtesting.domain;

import lombok.Data;

import java.util.List;

@Data
public class CustomerCriteriaCondition {
    public CustomerCriteriaCondition() {
    }

    public CustomerCriteriaCondition(List<String> segmentIds) {
        this.segmentIds = segmentIds;
    }

    private List<String> segmentIds;
}
