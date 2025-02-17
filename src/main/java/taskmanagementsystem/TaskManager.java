package taskmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

// Assumption: Otan kanoume update ena task kai allazoume to dueDate, allazei automata kai to triggerDate twn ypenthimisewn analoga me to type tous
// An to type einai custom kai to trigger Date einai meta to neo dueDate tote h ypnthimish diagrafetai
// Assumption: Otan diagrafetai mia ergasia diagrafontai kai oles oi ypenthymiseis
// Need: Java 8+ and Jackson library

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
    public Task createTask(String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate, TaskStatus status) {
        int newId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1; // Ensure unique ID
        Task task = new Task(newId, title, description, categoryId, priorityId, dueDate, status);
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
        notifications.removeIf(notification -> notification.getTaskId() == task.getId());
        for(Category category : categories) {
            if(category.getId().equals(task.getCategoryId())) {
                category.removeTask(task);
                break;
            }
        }
        for(Priority priority : priorities) {
            if(priority.getId().equals(task.getPriorityId())) {
                priority.removeTask(task);
                break;
            }
        }
        tasks.remove(task);
    }

    public void updateTask(Task task, String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate, TaskStatus status) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(status);

        if (status == TaskStatus.COMPLETED) {
            notifications.removeIf(notification -> notification.getTaskId().equals(task.getId()));
        }
        
        
        for(Notification notification : notifications) {
            if(notification.getTaskId().equals(task.getId())) {
                if(notification.getType() == NotificationType.DAY_BEFORE) {
                    notification.setTriggerDate(dueDate.minusDays(1));
                } else if(notification.getType() == NotificationType.WEEK_BEFORE) {
                    notification.setTriggerDate(dueDate.minusWeeks(1));
                } else if(notification.getType() == NotificationType.MONTH_BEFORE) {
                    notification.setTriggerDate(dueDate.minusMonths(1));
                }
            }
        }

        // In case there are multiple custom notifications
        // We dont want to be removing while iterating so we use removeIf separately from the above code block
        notifications.removeIf(notification -> 
            notification.getTaskId().equals(task.getId()) && 
            notification.getType() == NotificationType.CUSTOM && 
            (notification.getTriggerDate().isAfter(dueDate) || notification.getTriggerDate().isEqual(dueDate))
        );

        if(!task.getCategoryId().equals(categoryId)) {
            for(Category category : categories) {
                if(category.getId().equals(task.getCategoryId())) {
                    category.removeTask(task);
                }
                if(category.getId().equals(categoryId)) {
                    category.addTask(task);
                }
            }
        }
        if(!task.getPriorityId().equals(priorityId)) {
            for(Priority priority : priorities) {
                if(priority.getId().equals(task.getPriorityId())) {
                    priority.removeTask(task);
                }
                if(priority.getId().equals(priorityId)) {
                    priority.addTask(task);
                }
            }
        }
        task.setCategoryId(categoryId);
        task.setPriorityId(priorityId);
    }

    /**
     * Create a category
     * @param name the name of the category
     * @return the created category
     */
    public Category createCategory(String name) {
        int newId = categories.isEmpty() ? 1 : categories.get(categories.size() - 1).getId() + 1; // Ensure unique ID
        Category category = new Category(newId, name);
        this.categories.add(category);
        return category;
    }

    /**
     * Delete a category with all its tasks
     * @param category the category to delete
     */
    public void deleteCategory(Category category) {
        new ArrayList<>(category.getTasks()).forEach(this::deleteTask);
        category.deleteCategory();
        categories.remove(category);
    }

    /**
     * Create a priority
     * @param name the name of the priority
     * @return the created priority
     */
    public Priority createPriority(Integer id, String name) {
        int newId = priorities.isEmpty() ? 1 : priorities.get(priorities.size() - 1).getId() + 1; // Ensure unique ID
        Priority priority = new Priority(newId, name);
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
        int newId = notifications.isEmpty() ? 1 : notifications.get(notifications.size() - 1).getId() + 1; // Ensure unique ID
        Notification notification = new Notification(newId, type, triggerDate, task.getId(), task.getDueDate());
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
