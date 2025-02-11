package taskmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class TaskManager {
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Task> tasks;
    private List<Notification> notifications;

    /**
     * Constructor
     */
    public TaskManager() {
        this.categories = new ArrayList<Category>();
        this.priorities = new ArrayList<Priority>();
        this.tasks = new ArrayList<Task>();
        this.notifications = new ArrayList<Notification>();
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
    public Task createTask(Integer id, String title, String description, Integer categoryId, Integer priorityId, LocalDate dueDate) {
        Task task = new Task(id, title, description, categoryId, priorityId, dueDate);
        this.tasks.add(task);
        return task;     
    }

    // auxiliary method to delete a task
    private void deleteTask(Task task) {
        tasks.remove(task);

        for(Notification notification : notifications) {
            if(notification.getTaskId().equals(task.getId())) {
                notifications.remove(notification);
            }
        }
    }

    /**
     * Removes a task
     * @param taskTitle the title of the task to remove
     */
    public void deleteTask(String taskTitle) {
        Task taskToDelete = null;
        for(Task task : tasks) {
            if(task.getTitle().equals(taskTitle)) {
                taskToDelete = task;
                break;
            }
        }
        deleteTask(taskToDelete);
    }

    /**
     * Create a category
     * @param name the name of the category
     * @return the created category
     */
    public Category createCategory(Integer id, String name) {
        Category category = new Category(id, name);
        this.categories.add(category);
        return category;
    }

    /**
     * Delete a category with all its tasks
     * @param category the category to delete
     */
    public void deleteCategory(String categoryName) {
        Category categoryToDelete = null;
        for(Category category : categories) {
            if(category.getName().equals(categoryName)) {
                categoryToDelete = category;
                categories.remove(category); //Remove the category
                break;
            }
        }
        for(Task task : tasks) {
            if(task.getCategoryId().equals(categoryToDelete.getId())) {
                deleteTask(task); //Delete the task
            }
        }
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
    public void deletePriority(String priorityName) {
        Priority priorityToBeDeleted = null;
        for(Priority priority : priorities) {
            if(priority.getName().equals(priorityName)) {
                priorityToBeDeleted = priority; //The priority to delete
                priorities.remove(priority); //Remove the priority
                break;
            }
        }
        for(Task task : tasks) {
            if(task.getPriorityId().equals(priorityToBeDeleted.getId())) {
                task.setPriorityId(1); //Set the priority to default
            }
        }
    }

    public void addNotification(Integer id, NotificationType type, LocalDate triggerDate, String taskTitle) throws IllegalArgumentException {
        Task taskToAdd = null;
        for(Task task : tasks) {
            if(task.getTitle().equals(taskTitle)) {
                taskToAdd = task;
                break;
            }
        }
        Notification notification = new Notification(id, type, triggerDate, taskToAdd.getId(), taskToAdd.getDueDate());
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
        List<Task> results = new ArrayList<Task>();
        Category categoryToSearch = null;
        for(Category category : categories) {
            if(category.getName().equals(categoryName)) {
                categoryToSearch = category;
                break;
            }
        }
        for (Task task : tasks) {
            if (task.getCategoryId() == categoryToSearch.getId()) {
                results.add(task);
            }
        }
        return results;
    }

    /**
     * Finds a task by priority
     * @param priority the priority to search for
     */
    public List<Task> findTaskByPriority(String priorityName) {
        Priority priorityToSearch = null;
        List<Task> results = new ArrayList<Task>();

        for(Priority priority : priorities) {
            if(priority.getName().equals(priorityName)) {
                priorityToSearch = priority;
                break;
            }
        }
        for (Task task : tasks) {
            if (task.getPriorityId() == priorityToSearch.getId()) {
                results.add(task);
            }
        }
        return results;
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

    // Load data from JSON files
    public void loadData(String tasksPath, String categoriesPath, String prioritiesPath, String notificationsPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            tasks = mapper.readValue(new File(tasksPath), mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            categories = mapper.readValue(new File(categoriesPath), mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
            priorities = mapper.readValue(new File(prioritiesPath), mapper.getTypeFactory().constructCollectionType(List.class, Notification.class));
            notifications = mapper.readValue(new File(notificationsPath), mapper.getTypeFactory().constructCollectionType(List.class, Notification.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save data to JSON files
    public void saveData(String tasksPath, String categoriesPath, String prioritiesPath, String notificationsPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(tasksPath), tasks);
            mapper.writeValue(new File(categoriesPath), categories);
            mapper.writeValue(new File(prioritiesPath), priorities);
            mapper.writeValue(new File(notificationsPath), notifications);              
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
