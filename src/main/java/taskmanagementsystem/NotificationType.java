package taskmanagementsystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NotificationType {
    @JsonProperty("DAY_BEFORE") DAY_BEFORE,
    @JsonProperty("WEEK_BEFORE") WEEK_BEFORE,
    @JsonProperty("MONTH_BEFORE") MONTH_BEFORE,
    @JsonProperty("CUSTOM") CUSTOM;

    @Override
    public String toString() {
        switch (this) {
            case DAY_BEFORE:
                return "A Day Before";
            case WEEK_BEFORE:
                return "A Week Before";
            case MONTH_BEFORE:
                return "A Month Before";
            case CUSTOM:
                return "Pick a Date";
            default:
                return "";
        }
    }
}