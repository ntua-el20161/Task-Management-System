package taskmanagementsystem;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "triggerDate", "taskId", "dueDate", "type"})
public class Notification {
    private int id;
    private NotificationType type; // Type of notification

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate triggerDate; // Date when the notification should be triggered

    private Integer taskId; // Associated task

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate; // Due date of the associated task
    
    // Default constructor: Required for JSON deserialization
    Notification() {}

    
    Notification(int id, NotificationType type, LocalDate triggerDate, Integer taskId, LocalDate dueDate) throws IllegalArgumentException {
        this.id = id;
        this.type = type;
        this.triggerDate = triggerDate;
        this.taskId = taskId;
        this.dueDate = dueDate;

        
        switch (type) {
            case DAY_BEFORE:
                if(triggerDate.isAfter(this.dueDate.minusDays(1)) || triggerDate.isBefore(this.dueDate.minusDays(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one day before the due date.");
                }
                break;
            case WEEK_BEFORE:
                if(triggerDate.isAfter(this.dueDate.minusWeeks(1)) || triggerDate.isBefore(this.dueDate.minusWeeks(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one week before the due date.");
                }
                break;
            case MONTH_BEFORE:
                if(triggerDate.isAfter(this.dueDate.minusMonths(1)) || triggerDate.isBefore(this.dueDate.minusMonths(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one month before the due date.");
                }
                break;
            case CUSTOM:
                if(triggerDate.isAfter(this.dueDate) || triggerDate.isEqual(this.dueDate)) {
                    throw new IllegalArgumentException("The trigger date must be before the due date.");
                }
                break;
        }
    }

    public int getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) throws IllegalArgumentException {
        switch (type) {
            case DAY_BEFORE:
                triggerDate = dueDate.minusDays(1);
                break;
            case WEEK_BEFORE:
                triggerDate = dueDate.minusWeeks(1);
                break;
            case MONTH_BEFORE:
                triggerDate = dueDate.minusMonths(1);
                break;
            case CUSTOM:
                if(triggerDate.isAfter(dueDate) || triggerDate.isEqual(dueDate)) {
                    throw new IllegalArgumentException("The trigger date must be before the due date.");
                }
                break;
        }
        this.type = type;
    }

    public LocalDate getTriggerDate() {
        return triggerDate;
    }

    void setTriggerDate(LocalDate triggerDate) throws IllegalArgumentException {
        if(type == NotificationType.CUSTOM) {
            this.triggerDate = triggerDate;
        }
        else {
            throw new IllegalArgumentException("The trigger date can only be set for custom notifications.");
        }
    }
   
    public Integer getTaskId() {
        return taskId;
    }

    // Method to display notification details
    @Override
    public String toString() {
        return "Type: " + type.toString() +
               "\nTrigger Date: " + triggerDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Setter used ONLY during deserialization (without validation)
    @JsonSetter("triggerDate")
    public void setTriggerDateForJsonDeserialization(LocalDate triggerDate) {
        this.triggerDate = triggerDate;
    }
}
