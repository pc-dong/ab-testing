package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Segment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class CustomerSegments implements cn.dpc.abtesting.domain.CustomerSegments {

    @Override
    public Flux<Segment> findByCustomerId(String customerId) {
        return Flux.just(new Segment("1111"), new Segment("2222"));
    }
}
