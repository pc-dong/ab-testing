package cn.dpc.abtesting.api.rest;


import cn.dpc.abtesting.domain.Assignment;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("experiments/{experimentId}/assignments")
public class AssignmentApi {
    private final Experiments experiments;

    public AssignmentApi(@Qualifier("experimentsForClient") Experiments experiments) {
        this.experiments = experiments;
    }

    @GetMapping("{customerId}")
    public Mono<Assignment> getAssignment(@PathVariable("experimentId") String experimentId,
                                          @PathVariable("customerId") String customerId) {
        return experiments.findById(new Experiment.ExperimentId(experimentId))
                .flatMap(experiment -> experiment.assign(customerId));
    }
}
