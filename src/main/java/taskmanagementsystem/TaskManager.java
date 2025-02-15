package taskmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class TaskManager {
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Task> tasks;
    private List<Notification> notifications;
    private ObjectMapper mapper;

    private static final String DATA_FOLDER = Paths.get(System.getProperty("user.dir"), "src/main/medialab", "data").toString();
    private static final String TASKS_FILE = "tasks.json";  // Define the tasks file name
    private static final String CATEGORIES_FILE = "categories.json";  // Define the categories file name
    private static final String PRIORITIES_FILE = "priorities.json";  // Define the priorities file name
    private static final String NOTIFICATIONS_FILE = "notifications.json";  // Define the notifications file name

    /**
     * Constructor
     */
    public TaskManager() {
        this.categories = new ArrayList<Category>();
        this.priorities = new ArrayList<Priority>();
        this.tasks = new ArrayList<Task>();
        this.notifications = new ArrayList<Notification>();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Create a task
     * @param title
     * @param description
     * @param category
     * @param priority
     * @param dueDate
     * @return the created task
     */
    public Task createTask(String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate) {
        Task task = new Task(title, description, categoryId, priorityId, dueDate);
        this.tasks.add(task);
        for(Category category : categories) {
            if(category.getId().equals(categoryId)) {
                category.addTask(task);
                break;
            }
        }
        for(Priority priority : priorities) {
            if(priority.getId().equals(priorityId)) {
                priority.addTask(task);
                break;
            }
        }   
        return task;     
    }

    /**
     * Removes a task
     * @param taskTitle the title of the task to remove
     */
    public void deleteTask(Task task) {
        tasks.remove(task);

        for(Notification notification : notifications) {
            if(notification.getTaskId().equals(task.getId())) {
                notifications.remove(notification);
            }
        }
    }

    /**
     * Create a category
     * @param name the name of the category
     * @return the created category
     */
    public Category createCategory(String name) {
        Category category = new Category(name);
        this.categories.add(category);
        return category;
    }

    /**
     * Delete a category with all its tasks
     * @param category the category to delete
     */
    public void deleteCategory(Category category) {
        List<Task> tasks = category.getTasks();
        for(Task task : tasks) {
            deleteTask(task);
        }
        category.deleteCategory();
        categories.remove(category);
    }

    /**
     * Create a priority
     * @param name the name of the priority
     * @return the created priority
     */
    public Priority createPriority(Integer id, String name) {
        Priority priority = new Priority(id, name);
        this.priorities.add(priority);
        return priority;
    }

    /**
     * Deletes a priority and sets all tasks with this priority to default priority
     * @param priority the priority to delete
     */
    public void deletePriority(Priority priority) {
        List<Task> tasks = priority.getTasks();
        for(Task task : tasks) {
            task.setPriorityId(0);
            Priority.Default.addTask(task);
        }
        priority.deletePriority();
        priorities.remove(priority);
    }

    public void addNotification(NotificationType type, LocalDate triggerDate, Task task) throws IllegalArgumentException {
        Notification notification = new Notification(notifications.size()+1, type, triggerDate, task.getId(), task.getDueDate());
        notifications.add(notification);
    }

    public void deleteNotification(Notification notification) {
        notifications.remove(notification);
    }

    /**
     * Finds a task by title
     * @param title the title to search for
     */
    public Task findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Finds a task by category
     * @param category the category to search for
     */
    public List<Task> findTaskByCategory(String categoryName) {
        Category categoryToSearch = null;
        for(Category category : categories) {
            if(category.getName().equals(categoryName)) {
                categoryToSearch = category;
                break;
            }
        }
        if(categoryToSearch != null) {
            return categoryToSearch.getTasks();
        }
        return List.of();
    }

    /**
     * Finds a task by priority
     * @param priority the priority to search for
     */
    public List<Task> findTaskByPriority(String priorityName) {
        Priority priorityToSearch = null;

        for(Priority priority : priorities) {
            if(priority.getName().equals(priorityName)) {
                priorityToSearch = priority;
                break;
            }
        }
        if(priorityToSearch != null) {
            return priorityToSearch.getTasks();
        }
        return List.of();
    }

    /**
     * Get all tasks
     * @return the list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Get all categories
     * @return the list of categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Get all priorities
     * @return the list of priorities
     */
    public List<Priority> getPriorities() {
        return priorities;
    }

    /**
     * Get all notifications
     * @return the list of notifications
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    // Get tasks based on status
    public List<Task> getTasksByStatus(TaskStatus status) {
        List<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus().equals(status)) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    public void loadData() {
        File tasksFile = Paths.get(DATA_FOLDER, TASKS_FILE).toFile();
        File categoriesFile = Paths.get(DATA_FOLDER, CATEGORIES_FILE).toFile();
        File prioritiesFile = Paths.get(DATA_FOLDER, PRIORITIES_FILE).toFile();
        File notificationsFile = Paths.get(DATA_FOLDER, NOTIFICATIONS_FILE).toFile();

        if (!tasksFile.exists()) {
            System.out.println("Tasks.json file not found");
        }

        if (!categoriesFile.exists()) {
            System.out.println("Categories.json file not found");
        }

        if (!prioritiesFile.exists()) {
            System.out.println("Priorities.json file not found");
        }

        if (!notificationsFile.exists()) {
            System.out.println("Notifications.json file not found");
        }
        
        try {
            tasks = mapper.readValue(tasksFile, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            categories = mapper.readValue(categoriesFile, mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
            priorities = mapper.readValue(prioritiesFile, mapper.getTypeFactory().constructCollectionType(List.class, Priority.class));
            notifications = mapper.readValue(notificationsFile, mapper.getTypeFactory().constructCollectionType(List.class, Notification.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save data to JSON files
    public void saveData() {
        File tasksFile = Paths.get(DATA_FOLDER, TASKS_FILE).toFile();
        File categoriesFile = Paths.get(DATA_FOLDER, CATEGORIES_FILE).toFile();
        File prioritiesFile = Paths.get(DATA_FOLDER, PRIORITIES_FILE).toFile();
        File notificationsFile = Paths.get(DATA_FOLDER, NOTIFICATIONS_FILE).toFile();

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(tasksFile, tasks);
            System.out.println("Tasks saved successfully.");

            mapper.writerWithDefaultPrettyPrinter().writeValue(categoriesFile, categories);
            System.out.println("Categories saved successfully.");

            mapper.writerWithDefaultPrettyPrinter().writeValue(prioritiesFile, priorities);
            System.out.println("Priorities saved successfully.");

            mapper.writerWithDefaultPrettyPrinter().writeValue(notificationsFile, notifications);
            System.out.println("Notifications saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
