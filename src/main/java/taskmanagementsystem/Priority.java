package taskmanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Priority {
    private Integer id;
    private String name;
    private List<Task> tasks;
    
    // Default priority
    public static final Priority Default = new Priority(0, "Default");

    // Default constructor: Required for JSON deserialization
    public Priority() {}
    
    /**
     * Constructor
     * @param name the name of the priority
     */
    public Priority(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    /**
     * Get the ID of the priority
     * @return the ID of the priority
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the name of the priority
     * @return the name of the priority
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the priority
     * @param name the new name of the priority
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the tasks of the priority
     * @return the tasks of the priority
     */
    public List<Task> getTasks() {
        return tasks;
    }

    void addTask(Task task) {
        tasks.add(task);
    }

    void deletePriority() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}
