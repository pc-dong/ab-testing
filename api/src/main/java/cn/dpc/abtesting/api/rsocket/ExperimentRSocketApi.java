package cn.dpc.abtesting.api.rsocket;

import cn.dpc.abtesting.api.dto.ExperimentDto;
import cn.dpc.abtesting.api.rsocket.config.annotation.DeleteMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.GetMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.PostMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.PutMessageMapping;
import cn.dpc.abtesting.domain.Experiment;
import cn.dpc.abtesting.domain.Experiments;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("experiments")
public class ExperimentRSocketApi {
    private final Experiments experiments;

    @PostMessageMapping
    public Mono<ExperimentDto> add(@Payload ExperimentDto experimentDto) {
        return experiments.add(experimentDto.toExperiment())
                .flatMap(ExperimentDto::fromExperiment);
    }

    @GetMessageMapping
    public Flux<ExperimentDto> findAll() {
        return experiments.findAll()
                .flatMap(ExperimentDto::fromExperiment);
    }

    @GetMessageMapping("{id}")
    public Mono<ExperimentDto> findById(@DestinationVariable("id") String id) {
        return experiments.findById(new Experiment.ExperimentId(id))
                .flatMap(ExperimentDto::fromExperiment);
    }

    @PutMessageMapping("{id}")
    public Mono<ExperimentDto> update(@DestinationVariable("id") String id, @Payload ExperimentDto experimentDto) {
        return experiments.update(new Experiment.ExperimentId(id), experimentDto.toExperiment())
                .flatMap(ExperimentDto::fromExperiment);
    }

    @DeleteMessageMapping("{id}")
    public Mono<Void> delete(@DestinationVariable("id") String id) {
        return experiments.delete(new Experiment.ExperimentId(id))
                .then();
    }
}
