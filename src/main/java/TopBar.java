import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// This is the top bar of the application that shows the total tasks, completed tasks, delayed tasks, and tasks for the next 7 days 
public class TopBar extends HBox {

    private Label totalTasksLabel;
    private Label completedTasksLabel;
    private Label delayedTasksLabel;
    private Label sevenDaysTasksLabel;

    public TopBar() {
        // Initialize labels for total tasks
        Label totalTasksText = new Label("Total Tasks");
        totalTasksLabel = new Label("0");
        totalTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox totalTasksBox = new VBox(totalTasksText, totalTasksLabel);
        totalTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for completed tasks
        Label completedTasksText = new Label("Completed Tasks");
        completedTasksLabel = new Label("0");
        completedTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox completedTasksBox = new VBox(completedTasksText, completedTasksLabel);
        completedTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for delayed tasks
        Label delayedTasksText = new Label("Delayed Tasks");
        delayedTasksLabel = new Label("0");
        delayedTasksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox delayedTasksBox = new VBox(delayedTasksText, delayedTasksLabel);
        delayedTasksBox.setAlignment(Pos.CENTER);

        // Initialize labels for tasks for the next 7 days
        Label sevenDaysTasksText = new Label("Tasks for the next 7 days");
        sevenDaysTasksLabel = new Label("0");
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

    // Methods to update the labels
    public void setTotalTasks(int totalTasks) {
        totalTasksLabel.setText(Integer.toString(totalTasks));
    }

    public void setCompletedTasks(int completedTasks) {
        completedTasksLabel.setText(Integer.toString(completedTasks));
    }

    public void setDelayedTasks(int delayedTasks) {
        delayedTasksLabel.setText(Integer.toString(delayedTasks));
    }

    public void setSevenDaysTasks(int sevenDaysTasks) {
        sevenDaysTasksLabel.setText(Integer.toString(sevenDaysTasks));
    }
}