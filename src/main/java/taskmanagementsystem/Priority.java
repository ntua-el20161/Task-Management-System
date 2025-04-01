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
    Priority() {}
    
    // Default access: Only the TaskManager can create priorities
    Priority(Integer id, String name) {
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

    void addTask(Task task) {
        tasks.add(task);
    }

    void removeTask(Task task) {
        tasks.remove(task);
    }
    
    void deletePriority() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}
