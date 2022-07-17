package cn.dpc.abtesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerCriteriaResult {
    private String customerId;
    private boolean access;
}
