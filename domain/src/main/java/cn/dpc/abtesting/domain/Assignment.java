package cn.dpc.abtesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;


@Getter
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
    public static class AssignmentId{
        private String experimentId;
        private String customerId;
    }
}
