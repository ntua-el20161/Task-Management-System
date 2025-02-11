package taskmanagementsystem;

import java.time.LocalDate;

public class Notification {
    private int id;
    private NotificationType type; // Type of notification
    private LocalDate triggerDate; // Date when the notification should be triggered
    private Integer taskId; // Associated task
    private LocalDate dueDate; // Due date of the associated task
    
    Notification(int id, NotificationType type, LocalDate triggerDate, Integer taskId, LocalDate dueDate) throws IllegalArgumentException {
        this.id = id;
        this.type = type;
        this.triggerDate = triggerDate;
        this.taskId = taskId;
        this.dueDate = dueDate;

        switch (type) {
            case DAY_BEFORE:
                if(triggerDate.isAfter(dueDate.minusDays(1)) || triggerDate.isBefore(dueDate.minusDays(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one day before the due date.");
                }
                break;
            case WEEK_BEFORE:
                if(triggerDate.isAfter(dueDate.minusWeeks(1)) || triggerDate.isBefore(dueDate.minusWeeks(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one week before the due date.");
                }
                break;
            case MONTH_BEFORE:
                if(triggerDate.isAfter(dueDate.minusMonths(1)) || triggerDate.isBefore(dueDate.minusMonths(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one month before the due date.");
                }
                break;
            case CUSTOM:
                if(triggerDate.isAfter(dueDate) || triggerDate.isEqual(dueDate)) {
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
        this.type = type;

        switch (type) {
            case DAY_BEFORE:
                if(triggerDate.isAfter(dueDate.minusDays(1)) || triggerDate.isBefore(dueDate.minusDays(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one day before the due date.");
                }
                break;
            case WEEK_BEFORE:
                if(triggerDate.isAfter(dueDate.minusWeeks(1)) || triggerDate.isBefore(dueDate.minusWeeks(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one week before the due date.");
                }
                break;
            case MONTH_BEFORE:
                if(triggerDate.isAfter(dueDate.minusMonths(1)) || triggerDate.isBefore(dueDate.minusMonths(1))) {
                    throw new IllegalArgumentException("The trigger date must be exactly one month before the due date.");
                }
                break;
            case CUSTOM:
                if(triggerDate.isAfter(dueDate) || triggerDate.isEqual(dueDate)) {
                    throw new IllegalArgumentException("The trigger date must be before the due date.");
                }
                break;
        }
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
    public void setTriggerDate(LocalDate triggerDate) throws IllegalArgumentException {
        if(type == NotificationType.CUSTOM) {
            if(triggerDate.isAfter(dueDate) || triggerDate.isEqual(dueDate)) {
                throw new IllegalArgumentException("The trigger date must be before the due date.");
            }
            this.triggerDate = triggerDate;
        }
        else {
            throw new IllegalArgumentException("Only custom notifications can have a custom trigger date.");
        }
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
        return "Notification ID: " + id +
               "\nType: " + type +
               "\nTrigger Date: " + triggerDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
               "\nAssociated Task: " + taskId;
    }
}
