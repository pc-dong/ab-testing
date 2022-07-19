package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.CustomerCriteriaCondition;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerCriteriaConditionRef implements cn.dpc.abtesting.domain.CustomerCriteriaConditionRef {
    private final CustomerCriteriaCondition customerCriteriaCondition;
    @Override
    public Optional<CustomerCriteriaCondition> getCondition() {
        return Optional.ofNullable(customerCriteriaCondition);
    }
}
