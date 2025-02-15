import taskmanagementsystem.*;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// This is the top bar of the application that shows the total tasks, completed tasks, delayed tasks, and tasks for the next 7 days 
public class TopBar extends HBox {
    private TaskManager taskManager;
    private Label totalTasksLabel;
    private Label completedTasksLabel;
    private Label delayedTasksLabel;
    private Label sevenDaysTasksLabel;

    public TopBar(TaskManager taskManager) {
        this.taskManager = taskManager;
        // Initialize labels for total tasks
        Label totalTasksText = new Label("Total Tasks");
        totalTasksLabel = new Label(Integer.toString(taskManager.getTasks().size()));
        totalTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox totalTasksBox = new VBox(totalTasksText, totalTasksLabel);
        totalTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for completed tasks
        Label completedTasksText = new Label("Completed Tasks");
        completedTasksLabel = new Label(Integer.toString(taskManager.getTasksByStatus(TaskStatus.COMPLETED).size()));
        completedTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox completedTasksBox = new VBox(completedTasksText, completedTasksLabel);
        completedTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for delayed tasks
        Label delayedTasksText = new Label("Delayed Tasks");
        delayedTasksLabel = new Label(Integer.toString(taskManager.getTasksByStatus(TaskStatus.DELAYED).size()));
        delayedTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox delayedTasksBox = new VBox(delayedTasksText, delayedTasksLabel);
        delayedTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for tasks due in the next 7 days
        Label sevenDaysTasksText = new Label("Tasks due in the next 7 days");
        sevenDaysTasksLabel = new Label(Integer.toString((int) taskManager.getTasks().stream().filter(task -> task.getDueDate().isBefore(LocalDate.now().plusDays(7)) && task.getDueDate().isAfter(LocalDate.now())).count()));
        sevenDaysTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox sevenDaysTasksBox = new VBox(sevenDaysTasksText, sevenDaysTasksLabel);
        sevenDaysTasksBox.setAlignment(Pos.CENTER);
        
        // Add spacing between boxes
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        // Add labels to the HBox
        this.getChildren().addAll(totalTasksBox, spacer1, completedTasksBox, spacer2, delayedTasksBox, spacer3, sevenDaysTasksBox);

        // Add a little shift (margin) to the first and last boxes
        HBox.setMargin(totalTasksBox, new Insets(0, 0, 0, 20));
        HBox.setMargin(sevenDaysTasksBox, new Insets(0, 20, 0, 0));

        // Add styling (optional)
        this.setStyle("-fx-background-color: #2196F3; -fx-padding: 10;");
    }

    public void updateLabels() {
        totalTasksLabel.setText(Integer.toString(taskManager.getTasks().size()));
        completedTasksLabel.setText(Integer.toString(taskManager.getTasksByStatus(TaskStatus.COMPLETED).size()));
        delayedTasksLabel.setText(Integer.toString(taskManager.getTasksByStatus(TaskStatus.DELAYED).size()));
        sevenDaysTasksLabel.setText(Integer.toString((int) taskManager.getTasks().stream().filter(task -> task.getDueDate().isBefore(LocalDate.now().plusDays(7)) && task.getDueDate().isAfter(LocalDate.now())).count()));
    }
}