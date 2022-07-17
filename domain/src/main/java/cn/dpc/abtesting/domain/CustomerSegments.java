package cn.dpc.abtesting.domain;

import reactor.core.publisher.Flux;

public interface CustomerSegments {
    Flux<Segment> findByCustomerId(String customerId);
}
