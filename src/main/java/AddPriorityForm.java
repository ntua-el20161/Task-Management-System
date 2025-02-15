import taskmanagementsystem.*;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AddPriorityForm {

    private TaskManager taskManager;

    public AddPriorityForm (TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Priority");

        // Form fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        // Submit button
        Button submitButton = new Button("Add Priority");
        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            
            if (name.isEmpty()) {
                showAlert("Error", "Please fill name field.");
                return;
            }

            // Create the Priority
            taskManager.createPriority(taskManager.getPriorities().size()+1, name);
            
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            nameLabel, nameField,
            submitButton
        );
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout, 300, 400));
        window.showAndWait();
    }

    private void showAlert(String name, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(name);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

