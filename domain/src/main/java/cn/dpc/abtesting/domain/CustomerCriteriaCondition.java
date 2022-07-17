package cn.dpc.abtesting.domain;

import lombok.Data;

import java.util.List;

@Data
public class CustomerCriteriaCondition {
    private List<String> segmentIds;
}
