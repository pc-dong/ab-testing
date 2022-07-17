package cn.dpc.abtesting.domain;

import reactor.core.publisher.Mono;

public interface CustomerCriteriaResults {
    Mono<CustomerCriteriaResult> checkAssess(String customerId);
}
