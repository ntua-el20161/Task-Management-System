import taskmanagementsystem.*;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class AddTaskForm {

    private TaskManager taskManager;

    public AddTaskForm(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Task");

        // Form fields
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label descLabel = new Label("Description:");
        TextField descriptionField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();

        Label priorityLabel = new Label("Priority:");
        ComboBox<String> priorityComboBox = new ComboBox<>();

        Label dueDateLabel = new Label("Due Date:");
        DatePicker dueDatePicker = new DatePicker();

        List<Category> categories = taskManager.getCategories();
        List<Priority> priorities = taskManager.getPriorities();

        for(Category category : categories) {
            categoryComboBox.getItems().add(category.getName());
        }

        for(Priority priority : priorities) {
            priorityComboBox.getItems().add(priority.getName());
        }
        // Submit button
        Button submitButton = new Button("Add Task");
        submitButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String selectedCategory = categoryComboBox.getValue();
            String selectedPriority = priorityComboBox.getValue();
            LocalDate dueDate = dueDatePicker.getValue();

            if (title.isEmpty() || selectedCategory == null || selectedPriority == null || dueDate == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            Integer categoryId = null;
            Integer priorityId = null;
            for (Category category : categories) {
                if (category.getName().equals(selectedCategory)) {
                    categoryId = category.getId();
                    break;
                }
            }

            for (Priority priority : priorities) {
                if (priority.getName().equals(selectedPriority)) {
                    priorityId = priority.getId();
                    break;
                }
            }

            // Create the task
            taskManager.createTask(title, description, categoryId, priorityId, dueDate);

            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            titleLabel, titleField,
            descLabel, descriptionField,
            categoryLabel, categoryComboBox,
            priorityLabel, priorityComboBox,
            dueDateLabel, dueDatePicker,
            submitButton
        );
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout, 300, 400));
        window.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

