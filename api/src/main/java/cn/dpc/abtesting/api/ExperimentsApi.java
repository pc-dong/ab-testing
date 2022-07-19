package cn.dpc.abtesting.api;


import cn.dpc.abtesting.api.dto.ExperimentDto;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/experiments")
@RequiredArgsConstructor
public class ExperimentsApi {

    private final Experiments experiments;

    @PostMapping
    public Mono<ExperimentDto> add(@RequestBody ExperimentDto experimentDto) {
        return experiments.add(experimentDto.toExperiment())
                .flatMap(ExperimentDto::fromExperiment);
    }

    @GetMapping
    public Flux<ExperimentDto> findAll() {
        return experiments.findAll()
                .flatMap(ExperimentDto::fromExperiment);
    }

    @GetMapping("/{id}")
    public Mono<ExperimentDto> findById(@PathVariable("id") String id) {
        return experiments.findById(new Experiment.ExperimentId(id))
                .flatMap(ExperimentDto::fromExperiment);
    }

    @PutMapping("/{id}")
    public Mono<ExperimentDto> update(@PathVariable("id") String id, @RequestBody ExperimentDto experimentDto) {
        return experiments.update(new Experiment.ExperimentId(id), experimentDto.toExperiment())
                .flatMap(ExperimentDto::fromExperiment);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return experiments.delete(new Experiment.ExperimentId(id))
                .then();
    }
}
