package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.persistence.mapper.ModelMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
@Qualifier("experimentsForClient")
public class ExperimentsForClient extends Experiments {

    private static final Cache<Object, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(100).build();
    private final ModelMapper modelMapper;
    private final CustomerSegments customerSegments;

    public ExperimentsForClient(ModelMapper modelMapper, CustomerSegments customerSegments) {
        super(modelMapper, customerSegments);
        this.modelMapper = modelMapper;
        this.customerSegments = customerSegments;
    }

    @Override
    public Mono<Experiment> findById(Experiment.ExperimentId id) {
        String key = "experiment" + id.getId();
        return ((Mono) cache.get(key, (code) -> super.findById(id).cache()))
                .onErrorResume(e -> {
                    cache.invalidate(key);
                    return Mono.error((Throwable) e);
                });
    }

}
