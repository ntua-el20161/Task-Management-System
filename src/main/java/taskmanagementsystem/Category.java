package taskmanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Category {
    // Attributes
    private Integer id;
    private String name;
    private List<Task> tasks;
    
    // Default constructor: Required for JSON deserialization
    public Category() {}

    // Default access: Only the TaskManager can create categories
    Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    /**
     * Get the ID of the category
     * @return the ID of the category
     */
    public Integer getId() {
        return id;  
    }
    
    /**
     * Get the name of the category
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the category
     * @param name the new name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the tasks of the category
     * @return the tasks of the category
     */
    public List<Task> getTasks() {
        return tasks;
    }

    void addTask(Task task) {
        tasks.add(task);
    }

    void removeTask(Task task) {
        tasks.remove(task);
    }

    void deleteCategory() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}