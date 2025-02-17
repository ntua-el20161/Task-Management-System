import java.time.LocalDate;
import java.util.List;

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

public class EditTaskForm {
    private TaskManager taskManager;
    private Task task;

    public EditTaskForm(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;
        this.task = task;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Task");

        // Form fields
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField(task.getTitle());

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField(task.getDescription());

        Label dueDateLabel = new Label("Due Date:");
        DatePicker dueDatePicker = new DatePicker(task.getDueDate());

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        List<Category> categories = taskManager.getCategories();
        for(Category category : categories) {
            if(category.getId() == task.getCategoryId()) {
                categoryComboBox.setValue(category.getName());
                continue;
            }
            categoryComboBox.getItems().add(category.getName());
        }
        
        Label priorityLabel = new Label("Priority:");
        ComboBox<String> priorityComboBox = new ComboBox<>();
        List<Priority> priorities = taskManager.getPriorities();
        for(Priority priority : priorities) {
            if(priority.getId() == task.getPriorityId()) {
                priorityComboBox.setValue(priority.getName());
                continue;
            }
            priorityComboBox.getItems().add(priority.getName());
        }

        Label statusLabel = new Label("Status:");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Open", "In Progress", "Postponed", "Completed", "Delayed");
        statusComboBox.setValue(task.getStatus().toString());

        // Submit button
        Button submitButton = new Button("Edit Task");
        submitButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            LocalDate dueDate = dueDatePicker.getValue();
            Category category = null;
            Priority priority = null;
            String statusString = statusComboBox.getValue(); 

            if (title.isEmpty() || description.isEmpty() || dueDate == null || categoryComboBox.getValue() == null || priorityComboBox.getValue() == null || statusString == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            for (Category c : categories) {
                if (c.getName().equals(categoryComboBox.getValue())) {
                    category = c;
                    break;
                }
            }

            for (Priority p : priorities) {
                if (p.getName().equals(priorityComboBox.getValue())) {
                    priority = p;
                    break;
                }
            }

            TaskStatus status;
            if(statusString == "Open") {
                status = TaskStatus.OPEN;
            } else if(statusString == "In Progress") {
                status = TaskStatus.IN_PROGRESS;
            } else if(statusString == "Postponed") {
                status = TaskStatus.POSTPONED;
            } else if(statusString == "Completed") {
                status = TaskStatus.COMPLETED;
            } else {
                status = TaskStatus.DELAYED;
            }

            taskManager.updateTask(task, title, description, category.getId(), priority.getId(), dueDate, status);

            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            titleLabel, titleField,
            descriptionLabel, descriptionField,
            dueDateLabel, dueDatePicker,
            categoryLabel, categoryComboBox,
            priorityLabel, priorityComboBox,
            statusLabel, statusComboBox,
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
