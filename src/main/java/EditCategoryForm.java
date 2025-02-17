import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import taskmanagementsystem.*;

public class EditCategoryForm {
    private Category category;

    public EditCategoryForm(Category category) {
        this.category = category;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Category");

        // Form fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(category.getName());

        // Submit button
        Button submitButton = new Button("Edit Category");
        submitButton.setOnAction(event -> {
            String name = nameField.getText();


            if (name.isEmpty()) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            category.setName(name);
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
