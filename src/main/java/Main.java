import taskmanagementsystem.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Assumption: oi ypenthimiseis pou h prothesmia tous perase diagrafontai sthn arxikopoihsh. An mia ypenthimish einai smr o xrhsths lamvanei mhnytma

//TODO: javadoc
//TODO: comments
//TODO: Report
//TODO: maybe getCategoryById etc

public class Main extends Application {
    private TaskManager taskManager; 

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager();

        taskManager.loadData();

        // Title of tha main window of the app
        primaryStage.setTitle("Medialab Assistant");

        primaryStage.setWidth(950);  // Set width
        primaryStage.setHeight(600); // Set height

        // Update the status of tasks whose due date has passed
        updateDelayedTasks();

        // Delete passed notifications
        deleteExpiredNotifications();

        //------------------------------------------------------------------
        // Top bar section

        TopBar topBar = new TopBar(taskManager);
        

        // ------------------------------------------------------------------

        // Create the main content area
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));

        // Create a list view to display tasks
        CustomListView<Task> taskCustomListView = new CustomListView<>(taskManager, topBar);
        taskCustomListView.setupListView(Task.class, false);

        CustomListView<Category> categoryCustomListView = new CustomListView<>(taskManager, topBar);
        categoryCustomListView.setupListView(Category.class, false);

        CustomListView<Priority> priorityCustomListView = new CustomListView<>(taskManager, topBar);
        priorityCustomListView.setupListView(Priority.class, false);

        CustomListView<Notification> notificationCustomListView = new CustomListView<>(taskManager, topBar);
        notificationCustomListView.setupListView(Notification.class, false);

        CustomListView<Task> searchTaskCustomListView = new CustomListView<>(taskManager, topBar);
        searchTaskCustomListView.setupListView(Task.class, true);

        // Create a container to hold the list view
        VBox container = new VBox(10);
        container.setPadding(new Insets(20));

        //------------------------------------------------------------------
        // AddTask button

        Button addTaskButton = new Button("Add Task");

        // Add functionality to the "Add Task" button
        addTaskButton.setOnAction(event -> {

            AddTaskForm addTaskForm = new AddTaskForm(taskManager);
            addTaskForm.showForm();

            // Update the list view to reflect the new task
            taskCustomListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));

            //Update the top bar
            topBar.updateLabels();
        });
        
        // ------------------------------------------------------------------
        // AddCategory button

        Button addCategoryButton = new Button("Add Category");

        // Add functionality to the "Add Category" button
        addCategoryButton.setOnAction(event -> {

            AddCategoryForm addCategoryForm = new AddCategoryForm(taskManager);
            addCategoryForm.showForm();
        });

        // ------------------------------------------------------------------
        // AddPriority button

        Button addPriorityButton = new Button("Add Priority");

        // Add functionality to the "Add Priority" button
        addPriorityButton.setOnAction(event -> {

            AddPriorityForm addPriorityForm = new AddPriorityForm(taskManager);
            addPriorityForm.showForm();
        });

        // ------------------------------------------------------------------
        // Search bar section

        // Create the search bar
        Button searchButton = new Button("Search Tasks");

        // Add functionality to the search button
        searchButton.setOnAction(event -> {
            SearchForm searchForm = new SearchForm(taskManager, searchTaskCustomListView);
            searchForm.showForm();
            showView(searchTaskCustomListView, container);
        });

        //------------------------------------------------------------------
        // Show all tasks button

        Button showTasksButton = new Button("Show Tasks");

        // Add functionality to the "Show Tasks" button
        showTasksButton.setOnAction(event -> {
            taskCustomListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));
            showView(taskCustomListView, container);
        });

        //------------------------------------------------------------------
        // Show all categories button

        Button showCategoriesButton = new Button("Show Categories");

        // Add functionality to the "Show Categories" button
        showCategoriesButton.setOnAction(event -> {
            categoryCustomListView.setItems(FXCollections.observableArrayList(taskManager.getCategories()));
            showView(categoryCustomListView, container);
        });

        //------------------------------------------------------------------
        // Show all priorities button

        Button showPrioritiesButton = new Button("Show Priorities");

        // Add functionality to the "Show Priorities" button
        showPrioritiesButton.setOnAction(event -> {
            priorityCustomListView.setItems(FXCollections.observableArrayList(taskManager.getPriorities()));
            showView(priorityCustomListView, container);
        });

        //------------------------------------------------------------------
        // Show all notifications button

        Button showNotificationsButton = new Button("Show Notifications");

        // Add functionality to the "Show Notifications" button
        showNotificationsButton.setOnAction(event -> {
            notificationCustomListView.setItems(FXCollections.observableArrayList(taskManager.getNotifications()));
            showView(notificationCustomListView, container);
        });

        //------------------------------------------------------------------

        HBox topButtonsContainer = new HBox(10);
        topButtonsContainer.getChildren().addAll(searchButton, showTasksButton, showCategoriesButton, showPrioritiesButton, showNotificationsButton);

        HBox bottomButtonsContainer = new HBox(10); // 10 is spacing between buttons
        bottomButtonsContainer.getChildren().addAll(addTaskButton, addCategoryButton, addPriorityButton);

        mainContent.getChildren().addAll(topButtonsContainer, container, bottomButtonsContainer);

        // Create a BorderPane and add the top bar and main content
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(mainContent);

        // Create a scene with the BorderPane
        Scene scene = new Scene(root, 800, 600);

        // Set the scene to the stage (window)
        primaryStage.setScene(scene);

        // Show the stage (window)
        primaryStage.show();

        // Notify the user of delayed tasks
        notifyDelayedTasks();

        // Check for notifications at startup
        checkNotificationsAtStartup();
    
        taskCustomListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));
        showView(taskCustomListView, container);
    }

    public void notifyDelayedTasks() {
        int delayedCount = taskManager.getTasksByStatus(TaskStatus.DELAYED).size();

        if (delayedCount > 0) {
            // Ensure the alert runs on the JavaFX Application Thread
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Task Alert");
                alert.setHeaderText(null);
                alert.setContentText("You have " + delayedCount + " delayed task(s)!");
                alert.show(); // Show the alert without blocking
            });
        }
    }
    
    // Create method to switch views
    private void showView(CustomListView<?> customListView, VBox container) {
        container.getChildren().clear();
        container.getChildren().add(customListView.getListView());
    }
    
    private void updateDelayedTasks() {
            for (Task task : taskManager.getTasks()) {
            if (task.getDueDate().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.COMPLETED) {
                // Update task status to DELAYED
                taskManager.updateTask(task, task.getTitle(), task.getDescription(), task.getCategoryId(), task.getPriorityId(), task.getDueDate(), TaskStatus.DELAYED);
            }
        }
    }

    private void deleteExpiredNotifications() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Create a list to store the notifications to be removed
        List<Notification> notificationsToRemove = new ArrayList<>();

        // Iterate over the notifications and add expired ones to the list
        for (Notification notification : taskManager.getNotifications()) {
            if (notification.getTriggerDate().isBefore(today)) {
                notificationsToRemove.add(notification);
            }
        }

        // Remove the expired notifications from the taskManager
        taskManager.getNotifications().removeAll(notificationsToRemove);
    }

    private void checkNotificationsAtStartup() {
        LocalDate today = LocalDate.now();
        List<Notification> notifications = taskManager.getNotifications();

        for (Notification notification : notifications) {
            if (notification.getTriggerDate().equals(today)) {
                showNotificationAlert(notification);
            }
        }
    }

    private void showNotificationAlert(Notification notification) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Reminder");
        for(Task task : taskManager.getTasks()) {
            if(task.getId() == notification.getTaskId()) {
                alert.setHeaderText("Reminder for: " + task.getTitle());
                alert.setContentText("Task: " + task.getTitle() + "\n" + "Due Date: " + task.getDueDate());
                break;
            }
        }
        alert.showAndWait();
    }
    @Override
    public void stop() {
        System.out.println("Application is closing. Saving data...");
        taskManager.saveData();
    }
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);

        System.out.println("Welcome to the Task Management System!");
    }
}