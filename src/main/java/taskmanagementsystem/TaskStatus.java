package taskmanagementsystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatus {
    @JsonProperty("OPEN") OPEN,
    @JsonProperty("IN_PROGRESS") IN_PROGRESS,
    @JsonProperty("POSTPONED") POSTPONED,
    @JsonProperty("COMPLETED") COMPLETED,
    @JsonProperty("DELAYED") DELAYED;
}