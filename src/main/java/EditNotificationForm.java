import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import taskmanagementsystem.*;

public class EditNotificationForm {
    private Notification notification;

    public EditNotificationForm(Notification notification) {
        this.notification = notification;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Notification");

        Label typeLabel = new Label("Day to be notified:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("A Day Before", "A Week Before", "A Month Before", "Pick a Date");

        DatePicker customDatePicker = new DatePicker(); 

        typeComboBox.setOnAction(event -> {
            String selected = typeComboBox.getValue();
            if (selected.equals("Pick a Date")) {
                customDatePicker.setDisable(false);
            } else {
                customDatePicker.setDisable(true);
            }
        });
        
        // Submit button
        Button submitButton = new Button("Save");
        submitButton.setOnAction(event -> {
            
            String typeString = typeComboBox.getValue();
            LocalDate triggerDate = customDatePicker.getValue();

            if (typeString == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            if(typeString == "A Day Before") {
                notification.setType(NotificationType.DAY_BEFORE);
            } else if(typeString == "A Week Before") {
                notification.setType(NotificationType.WEEK_BEFORE);
            } else if(typeString == "A Month Before") {
                notification.setType(NotificationType.MONTH_BEFORE);
            } else {
                notification.setType(NotificationType.CUSTOM);
                if(triggerDate == null) {
                    showAlert("Error", "Please select a date.");
                    return;
                }
            }
            
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            typeLabel, typeComboBox, 
            customDatePicker,
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
