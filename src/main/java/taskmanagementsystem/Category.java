package taskmanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Category {
    // Attributes
    private Integer id;
    private String name;
    private List<Task> tasks;
    
    static Integer nextId = 11;
    // Default constructor: Required for JSON deserialization
    public Category() {}

    // Default access: Only the TaskManager can create categories
    Category(String name) {
        this.id = nextId++;
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

    void deleteCategory() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}