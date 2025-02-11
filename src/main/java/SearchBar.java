import taskmanagementsystem.*;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar extends HBox {
    private TextField searchField;
    private Button searchButton;
    private TaskManager taskManager;

    public SearchBar(TaskManager taskManager) {
        this.taskManager = taskManager;
        List<Task> filteredTasks = new ArrayList<Task>();

        // Initialize the search field and button
        searchField = new TextField();
        searchField.setPromptText("Search by title, category, or priority");

        searchButton = new Button("Search");

        // Add the search field and button to the HBox
        this.getChildren().addAll(searchField, searchButton);
        this.setSpacing(10); // Add spacing between components

        // Add search functionality
        searchButton.setOnAction(event -> {
            String searchText = searchField.getText().toLowerCase();
            
            filteredTasks.addAll(this.taskManager.findTaskByCategory(searchText));
            filteredTasks.addAll(this.taskManager.findTaskByPriority(searchText));
            filteredTasks.add(this.taskManager.findTaskByTitle(searchText));
        });
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }
}