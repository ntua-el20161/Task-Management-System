import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ser.std.InetAddressSerializer;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import taskmanagementsystem.*;

public class SearchForm {
    private Button searchButton;

    private TaskManager taskManager;
    ListView<Task> taskListView;

    public SearchForm(TaskManager taskManager, ListView<Task> taskListView) {
        this.taskManager = taskManager;
        this.taskListView = taskListView;
    }

    public void showForm() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Search a Task");
        Label titleLabel = new Label("Search by title:");
        TextField titleField = new TextField();

        Label categoryLabel = new Label("Search by category:");
        TextField categoryField = new TextField();

        Label priorityLabel = new Label("Search by priority:");
        TextField priorityField = new TextField();

        searchButton = new Button("Search");

        searchButton.setOnAction(event -> {
            List<Task> results = new ArrayList<>();
            String title = titleField.getText();
            String category = categoryField.getText();
            String priority = priorityField.getText();
            
            if(!title.isEmpty() && category.isEmpty() && priority.isEmpty()) {
                results.add(taskManager.findTaskByTitle(title));
            } else if(title.isEmpty() && !category.isEmpty() && priority.isEmpty()) {
                results.addAll(taskManager.findTaskByCategory(category));
            } else if(title.isEmpty() && category.isEmpty() && !priority.isEmpty()) {
                results.addAll(taskManager.findTaskByPriority(priority));
            } else if(!title.isEmpty() && !category.isEmpty() && priority.isEmpty()) {
                Task task = taskManager.findTaskByTitle(title);
                Integer categoryId = null;
                for(Category i : taskManager.getCategories()) {
                    if(i.getName().equals(category)) {
                        categoryId = i.getId();
                    }
                }
                if(task.getCategoryId() == categoryId) {
                    results.add(task);
                }
            } else if(!title.isEmpty() && category.isEmpty() && !priority.isEmpty()) {
                Task task = taskManager.findTaskByTitle(title);
                Integer priorityId = null;
                for(Priority i : taskManager.getPriorities()) {
                    if(i.getName().equals(priority)) {
                        priorityId = i.getId();
                    }
                }
                if(task.getPriorityId() == priorityId) {
                    results.add(task);
                }
            } else if(title.isEmpty() && !category.isEmpty() && !priority.isEmpty()) {
                List<Task> tasks = taskManager.findTaskByCategory(category);
                Integer priorityId = null;
                for(Priority i : taskManager.getPriorities()) {
                    if(i.getName().equals(priority)) {
                        priorityId = i.getId();
                    }
                }
                for(Task i : tasks) {
                    if(i.getPriorityId() == priorityId) {
                        results.add(i);
                    }
                }
            } else if(!title.isEmpty() && !category.isEmpty() && !priority.isEmpty()) {
                Task task = taskManager.findTaskByTitle(title);
                Integer categoryId = null;
                for(Category i : taskManager.getCategories()) {
                    if(i.getName().equals(category)) {
                        categoryId = i.getId();
                    }
                }
                Integer priorityId = null;
                for(Priority i : taskManager.getPriorities()) {
                    if(i.getName().equals(priority)) {
                        priorityId = i.getId();
                    }
                }
                if(task.getCategoryId() == categoryId && task.getPriorityId() == priorityId) {
                    results.add(task);
                }
            } else {
                results.addAll(taskManager.getTasks());
            }

            taskListView.setItems(FXCollections.observableArrayList(results));
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            titleLabel, titleField,
            categoryLabel, categoryField,
            priorityLabel, priorityField,
            searchButton
        );
        layout.setPadding(new Insets(10));

        window.setScene(new Scene(layout, 300, 400));
        window.showAndWait();
    }
}