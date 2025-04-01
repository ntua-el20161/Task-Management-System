package taskmanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Category {
    // Attributes
    private Integer id;
    private String name;
    private List<Task> tasks;
    
    // Default constructor: Required for JSON deserialization
    Category() {}

    // Default access: Only the TaskManager can create categories
    Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public Integer getId() {
        return id;  
    }
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // add a task to the category
    void addTask(Task task) {
        tasks.add(task);
    }

    // remove a task from the category
    void removeTask(Task task) {
        tasks.remove(task);
    }

    // remove all tasks from the category
    void deleteCategory() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}