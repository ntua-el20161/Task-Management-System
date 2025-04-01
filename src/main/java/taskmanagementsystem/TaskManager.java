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
     * Constructor. Initializes a list of categories, priorities, tasks and notifications
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
     * Create a task. The task is added to the list of tasks and the corresponding category and priority.
     * @param title
     * @param description
     * @param categoryId
     * @param priorityId
     * @param dueDate
     * @param status
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
     * Delete a task. It removes the task from the list of tasks, the corresponding category and priority and deletes all of its notifications.
     * @param taskTitle the title of the task to delete
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

    /**
     * Method to update a task. It updates the title, description, category, priority, due date and status of the task.
     * It uses the default access setters of the Task class and performs any necessary updates to the categories, priorities and notifications.
     * @param Task the task to update
     * @param Title the new title
     * @param description the new description
     * @param categoryId the new category id
     * @param priorityId the new priority id
     * @param dueDate the new due date
     * @param status the new status
     */
    public void updateTask(Task task, String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate, TaskStatus status) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(status);

        // if the task is completed, remove all notifications
        if (status == TaskStatus.COMPLETED) {
            notifications.removeIf(notification -> notification.getTaskId().equals(task.getId()));
        }
        
        // Update the trigger date of the notifications
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

        // Update the category and priority of the task
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
     * Create a category. The category is added to the list of categories.
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
     * Delete a category and all its tasks
     * @param category the category to delete
     */
    public void deleteCategory(Category category) {
        new ArrayList<>(category.getTasks()).forEach(this::deleteTask);
        category.deleteCategory();
        categories.remove(category);
    }

    /**
     * Create a priority. The priority is added to the list of priorities.
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

    /**
     * Add a notification to a task. The task must not have Status COMPLETED. This is ensured by the GUI.
     * @param type
     * @param triggerDate
     * @param task
     */
    public void addNotification(NotificationType type, LocalDate triggerDate, Task task) {
        int newId = notifications.isEmpty() ? 1 : notifications.get(notifications.size() - 1).getId() + 1; // Ensure unique ID
        Notification notification = new Notification(newId, type, triggerDate, task.getId(), task.getDueDate());
        notifications.add(notification);
    }

    /**
     * Delete a notification
     * @param notification the notification to delete
     */
    public void deleteNotification(Notification notification) {
        notifications.remove(notification);
    }

    /**
     * Finds a task by title
     * @param title the title to search for
     * @return the task with the given title or null if not found
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
     * Finds the tasks of a category
     * @param categoryName the category to search 
     * @return A list of the tasks of the category or an empty list if category not found
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
     * Finds the tasks of a priority
     * @param priorityName the priority to search
     * @return A list of the tasks of the priority or an empty list if priority not found
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

    /**
     * Get all tasks with a specific status
     * @param status
     * @return  the list of tasks with the given status
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        List<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus().equals(status)) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    /**
     * Load data from JSON files. Located in src/main/medialab/data folder
     */
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

    /**
     * Save data to JSON files. Located in src/main/medialab/data folder
     */
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
