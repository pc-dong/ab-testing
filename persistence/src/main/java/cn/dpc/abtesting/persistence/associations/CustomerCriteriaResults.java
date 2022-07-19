package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.CustomerCriteriaResult;
import cn.dpc.abtesting.domain.CustomerSegments;
import cn.dpc.abtesting.domain.Segment;
import cn.dpc.abtesting.domain.CustomerCriteriaConditionRef;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class CustomerCriteriaResults implements cn.dpc.abtesting.domain.CustomerCriteriaResults {
    private final CustomerSegments customerSegments;
    private final CustomerCriteriaConditionRef condition;

    @Override
    public Mono<CustomerCriteriaResult> checkAssess(String customerId) {
        return customerSegments.findByCustomerId(customerId)
                .map(Segment::getSegmentId)
                .any(segId -> condition.getCondition()
                        .map(con -> con.getSegmentIds().contains(segId))
                        .orElse(false))
                .map(access -> new CustomerCriteriaResult(customerId, access));
    }
}
