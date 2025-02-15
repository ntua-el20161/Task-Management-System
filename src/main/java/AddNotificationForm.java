import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import taskmanagementsystem.*;

public class AddNotificationForm {
    private TaskManager taskManager;
    private Task task;

    public AddNotificationForm(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;
        this.task = task;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Notification");

        // Form fields
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Day Before", "Week Before", "Month Before", "Custom");

        DatePicker customDatePicker = new DatePicker(); 
        
        // Submit button
        Button submitButton = new Button("Add Notification");
        submitButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String type = typeComboBox.getValue();
            LocalDate dueDate = customDatePicker.getValue();


            if (title.isEmpty() || description.isEmpty() || type == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            // Create the notification
            taskManager.createNotification(title, description, dueDate);

            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            titleLabel, titleField,
            descriptionLabel, descriptionField,
            submitButton
        );
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout, 300, 400));
        window.showAndWait();
    }

    private void updateDatePicker() {
        if (type == Type.DAY_BEFORE) {
            datePicker.setValue(LocalDate.now().minusDays(1)); // Set to yesterday
            datePicker.setDisable(true); // Lock it
        } else {
            datePicker.setDisable(false); // Enable it
        }
    }

    private void showAlert(String name, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(name);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
