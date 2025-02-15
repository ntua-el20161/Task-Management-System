import taskmanagementsystem.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        //------------------------------------------------------------------
        // Top bar section

        TopBar topBar = new TopBar(taskManager);

        // ------------------------------------------------------------------

        // Create the main content area
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));

        // Create a list view to display tasks
        ListView<Task> taskListView = new ListView<>();
        taskListView.setPrefHeight(400);

        ListView<Category> categoryListView = new ListView<>();
        categoryListView.setPrefHeight(400);

        ListView<Priority> priorityListView = new ListView<>();
        priorityListView.setPrefHeight(400);

        ListView<Notification> notificationListView = new ListView<>();
        notificationListView.setPrefHeight(400);

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
            taskListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));

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

        // TODO: add contains text search
        // Add functionality to the search button
        searchButton.setOnAction(event -> {
            SearchForm searchForm = new SearchForm(taskManager, taskListView);
            searchForm.showForm();
        });

        //------------------------------------------------------------------
        // Show all tasks button

        Button showTasksButton = new Button("Show Tasks");

        // Add functionality to the "Show Tasks" button
        showTasksButton.setOnAction(event -> {
            taskListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));
            showView(taskListView, container);
        });

        //------------------------------------------------------------------
        // Show all categories button

        Button showCategoriesButton = new Button("Show Categories");

        // Add functionality to the "Show Categories" button
        showCategoriesButton.setOnAction(event -> {
            List<Category> categories = taskManager.getCategories();
            
            categoryListView.setItems(FXCollections.observableArrayList(categories));
            showView(categoryListView, container);
        });

        //------------------------------------------------------------------
        // Show all priorities button

        Button showPrioritiesButton = new Button("Show Priorities");

        // Add functionality to the "Show Priorities" button
        showPrioritiesButton.setOnAction(event -> {
            List<Priority> priorities = taskManager.getPriorities();
            
            priorityListView.setItems(FXCollections.observableArrayList(priorities));
            showView(priorityListView, container);
        });

        //------------------------------------------------------------------
        // Show all notifications button

        Button showNotificationsButton = new Button("Show Notifications");

        // Add functionality to the "Show Notifications" button
        showNotificationsButton.setOnAction(event -> {
            List<Notification> notifications = taskManager.getNotifications();
            
            notificationListView.setItems(FXCollections.observableArrayList(notifications));
            showView(notificationListView, container);
        });

        //------------------------------------------------------------------

        HBox topButtonsContainer = new HBox(10);
        topButtonsContainer.getChildren().addAll(searchButton, showCategoriesButton, showPrioritiesButton, showNotificationsButton, showTasksButton);

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

        notifyDelayedTasks();

        taskListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));
        showView(taskListView, container);
    }

    // Method to notify the user of delayed tasks with a pop-up alert
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
    public void showView(ListView<?> listView, VBox container) {
        container.getChildren().clear();
        container.getChildren().add(listView);
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