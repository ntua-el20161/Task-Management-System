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
    public Notification() {}

    
    public Notification(int id, NotificationType type, LocalDate triggerDate, Integer taskId, LocalDate dueDate) throws IllegalArgumentException {
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

    /**
     * Get the ID of the notification
     * @return the ID of the notification
     */
    public int getId() {
        return id;
    }

    /**
     * Get the type of the notification
     * @return the type of the notification
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Set the type of the notification
     * @param type the new type of the notification
     */
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
    
    /**
     * Get the trigger date of the notification
     * @return the trigger date of the notification
     */
    public LocalDate getTriggerDate() {
        return triggerDate;
    }

    /**
     * Set the trigger date of the notification. Only for custom notifications.
     * @param triggerDate the new trigger date of the notification
     */
    void setTriggerDate(LocalDate triggerDate) throws IllegalArgumentException {
        this.triggerDate = triggerDate;
    }
   
    /**
     * Get the task id associated with the notification
     * @return the task id associated with the notification
     */
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
