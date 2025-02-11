package taskmanagementsystem;

import java.time.LocalDate;

// Assumption 1: The user cannot change the category of a Task
// Assumption 2: The user can change the priority of a Task

public class Task {
    private enum Status {
        OPEN, IN_PROGRESS, POSTPONED, COMPLETED, DELAYED
    }   
    // Attributes
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private Integer priorityId;
    private LocalDate dueDate;
    private Status status;
    
    // Default access constructor: Tasks can only be created by the TaskManager
    Task(Integer id,String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        this.status = Status.OPEN;
    }

    /**
     * Get the ID of the task
     * @return the ID of the task
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the title of the task
     * @return the title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the task
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the description of the task
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the task
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the category id of the task
     * @return the category id of the task
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Get the priority id of the task
     * @return the priority id of the task
     */
    public Integer getPriorityId() {
        return priorityId;
    }

    /**
     * Set the priority id of the task
     * @param priorityId the new priority id of the task
     */
    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    /**
     * Get the due date of the task
     * @return the due date of the task
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Set the due date of the task
     * @param dueDate the new due date of the task
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Get the status of the task
     * @return the status of the task
     */
    public Status getStatus() {
        return status;
    }

    @Override
    public String toString () {
        return "Task: " + title;
    }
}