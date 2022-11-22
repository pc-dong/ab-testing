package cn.dpc.abtesting.api.rsocket;

import cn.dpc.abtesting.api.rsocket.config.annotation.GetMessageMapping;
import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("experiments.{experimentId}.assignments")
public class AssignmentRSocketApi {
    private final Experiments experiments;

    public AssignmentRSocketApi(@Qualifier("experimentsForClient") Experiments experiments) {
        this.experiments = experiments;
    }

    @GetMessageMapping("{customerId}")
    public Mono<Assignment> getAssignment(@DestinationVariable("experimentId") String experimentId,
                                          @DestinationVariable("customerId") String customerId) {
        return experiments.findById(new Experiment.ExperimentId(experimentId))
                .flatMap(experiment -> experiment.assign(customerId));
    }
}
