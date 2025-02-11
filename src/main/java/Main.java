import taskmanagementsystem.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TaskManager taskManager = new TaskManager();

        // Title of tha main window of the app
        primaryStage.setTitle("Medialab Assistant");

        primaryStage.setWidth(950);  // Set width
        primaryStage.setHeight(600); // Set height

        // Create the top bar
        TopBar topBar = new TopBar();
        topBar.setTotalTasks(10);       // Example: Set total tasks
        topBar.setCompletedTasks(5);    // Example: Set completed tasks
        topBar.setDelayedTasks(2);      // Example: Set delayed tasks
        topBar.setSevenDaysTasks(3);    // Example: Set tasks for the next 7 days

         // Create the main content area
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));

        // Create a list view to display tasks
        ListView<Task> taskListView = new ListView<>();
        taskListView.setPrefHeight(400);

        Button addTaskButton = new Button("Add Task");

        // Create a filtered list for search results
        List<Task> filteredTasks = new ArrayList<>();

        // Create the search bar
        SearchBar searchBar = new SearchBar(taskManager);

        // Add the search bar and list view to the main content area
        mainContent.getChildren().addAll(searchBar, taskListView, addTaskButton);

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

        taskManager.createCategory(1, "STYLESHEET_CASPIAN");
        taskManager.createCategory(2, "STYLESHEET_MODENA");
        taskManager.createCategory(3, "STYLESHEET_UNITY");

        taskManager.createPriority(2, "Low");
        taskManager.createPriority(3, "Medium");
        taskManager.createPriority(4, "High");

        taskManager.createTask(1, "Task 1", "Description 1", 1, 2, LocalDate.now());
        taskManager.createTask(2, "Task 2", "Description 2", 1, 2, LocalDate.now());
        taskManager.createTask(3, "Task 3", "Description 3", 1, 2, LocalDate.now());

        /*
        // Update the list view with all tasks initially
        taskListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));

        // Bind the filtered tasks to the list view
        searchBar.getSearchButton().setOnAction(event -> {
            taskListView.setItems(FXCollections.observableArrayList(filteredTasks));
        });
        */

        // Add functionality to the "Add Task" button
        addTaskButton.setOnAction(event -> {
            // Add the new task to the task manager
            taskManager.createTask(5, "STYLESHEET_MODENA", "lol", 1, 1, LocalDate.now());
            
            // Update the list view to reflect the new task
            taskListView.setItems(FXCollections.observableArrayList(taskManager.getTasks()));
        });
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);

        System.out.println("Welcome to the Task Management System!");
    }
}