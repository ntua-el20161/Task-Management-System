package taskmanagementsystem;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

// Assumption 1: The user cannot change the category of a Task
// Assumption 2: The user can change the priority of a Task

public class Task {
    // Attributes
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private Integer priorityId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private TaskStatus status;
    
    // Default constructor: Required for JSON deserialization
    public Task() {}

    // Default access constructor: Tasks can only be created by the TaskManager
    public Task(Integer id, String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        if(status == null)
            this.status = TaskStatus.OPEN;
        else 
            this.status = status;
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

    // auxiliary method to change the category of a task
    // the task need to be added in the new category list
    void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    
    /**
     * Get the priority id of the task
     * @return the priority id of the task
     */
    public Integer getPriorityId() {
        return priorityId;
    }

    // auxiliary method to change the priority of a task
    // the task need to be added in the new priority list
    void setPriorityId(Integer priorityId) {
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
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the task
     * @param status the new status of the task
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString () {
        return "Task: " + title;
    }
}