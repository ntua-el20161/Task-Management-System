package taskmanagementsystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NotificationType {
    @JsonProperty("DAY_BEFORE") DAY_BEFORE,
    @JsonProperty("WEEK_BEFORE") WEEK_BEFORE,
    @JsonProperty("MONTH_BEFORE") MONTH_BEFORE,
    @JsonProperty("CUSTOM") CUSTOM;
}