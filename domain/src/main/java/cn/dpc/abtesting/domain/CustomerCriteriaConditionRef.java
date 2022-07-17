package cn.dpc.abtesting.domain;

import java.util.Optional;

public interface CustomerCriteriaConditionRef {
    Optional<CustomerCriteriaCondition> getCondition();
}
