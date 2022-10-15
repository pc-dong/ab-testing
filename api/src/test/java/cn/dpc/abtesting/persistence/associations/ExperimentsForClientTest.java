package cn.dpc.abtesting.persistence.associations;

import cn.dpc.abtesting.domain.Experiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperimentsForClientTest {
    @Mock
    ModelMapper modelMapper;

    @Mock
    CustomerSegments customerSegments;

    Experiments experiments;

    @BeforeEach
    public void init() {
        experiments = new ExperimentsForClient(modelMapper, customerSegments);
    }



    @Test
    void findById() {
        when(modelMapper.findExperimentById(anyString())).thenReturn(Mono.just(new Experiment()));

        experiments.findById(new Experiment.ExperimentId("1111")).block();
        experiments.findById(new Experiment.ExperimentId("1111")).block();

        verify(modelMapper, times(1)).findExperimentById(anyString());
    }
}
