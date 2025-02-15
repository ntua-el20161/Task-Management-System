import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import taskmanagementsystem.*;

public class CustomListView<T> {
    private TaskManager taskManager;
    private ListView<T> listView;
    
    public CustomListView(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.listView = new ListView<>();
    }

    public ListView<T> getListView() {
        return listView;
    }

    public void setupListView(Class<T> type) {
        listView.setCellFactory(listView -> new ListCell<T>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonContainer = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    T item = getItem();
                    if (item != null) {
                        editItem(item);
                    }
                });

                deleteButton.setOnAction(event -> {
                    T item = getItem();
                    if (item != null) {
                        deleteItem(item);
                    }
                });
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString()); // Use toString() or customize
                    setGraphic(buttonContainer);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void editItem(T item) {
        if (item instanceof Task) {
            new EditTaskForm(taskManager, (Task) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getTasks()));
        } else if (item instanceof Category) {
            new EditCategoryForm(taskManager, (Category) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getCategories()));
        } else if (item instanceof Priority) {
            new EditPriorityForm(taskManager, (Priority) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getPriorities()));
        } else if (item instanceof Notification) {
            new EditNotificationForm(taskManager, (Notification) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getNotifications()));
        }
    }

    @SuppressWarnings("unchecked")
    private void deleteItem(T item) {
        if (item instanceof Task) {
            taskManager.deleteTask((Task) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getTasks()));
        } else if (item instanceof Category) {
            taskManager.deleteCategory((Category) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getCategories()));
        } else if (item instanceof Priority) {
            taskManager.deletePriority((Priority) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getPriorities()));
        } else if (item instanceof Notification) {
            taskManager.deleteNotification((Notification) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getNotifications()));
        }
    }
}
