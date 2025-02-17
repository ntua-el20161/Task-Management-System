package taskmanagementsystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatus {
    @JsonProperty("OPEN") OPEN,
    @JsonProperty("IN_PROGRESS") IN_PROGRESS,
    @JsonProperty("POSTPONED") POSTPONED,
    @JsonProperty("COMPLETED") COMPLETED,
    @JsonProperty("DELAYED") DELAYED;

    @Override
    public String toString() {
        switch (this) {
            case OPEN:
                return "Open";
            case IN_PROGRESS:
                return "In progress";
            case POSTPONED:
                return "Postponed";
            case COMPLETED:
                return "Completed";
            case DELAYED:
                return "Delayed";
            default:
                return "";
        }
    }
}