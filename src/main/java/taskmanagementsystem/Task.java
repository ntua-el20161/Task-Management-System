package taskmanagementsystem;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    Task() {}

    // Default access constructor: Tasks can only be created by the TaskManager
    Task(Integer id, String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate, TaskStatus status) {
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

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    // auxiliary method to change the category of a task
    // the task need to be added in the new category list
    void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    // auxiliary method to change the priority of a task
    // the task need to be added in the new priority list
    void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // auxiliary method to change the due date of a task
    // the notifications must also change
    void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    // auxiliary method to change the status of a task
    // if the status changes to COMPLETED the notifications of the task must be deleted
    void setStatus(TaskStatus status) {
        this.status = status;
    }


    @Override
    public String toString () { 
        return "Task: " + title;
    }
}