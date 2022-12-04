package cn.dpc.abtesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    private AssignmentId id;
    private Optional<String> bucketKey;
    private Optional<String> config;
    private LocalDateTime assignedTime = LocalDateTime.now();

    public Assignment(AssignmentId id, String bucketKey, String config) {
        this.id = id;
        this.bucketKey = Optional.ofNullable(bucketKey);
        this.config = Optional.ofNullable(config);
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class AssignmentId{
        private String experimentId;
        private String customerId;
    }
}
