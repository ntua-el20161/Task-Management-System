import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import taskmanagementsystem.*;

public class CustomListView<T> {
    private TaskManager taskManager;
    private ListView<T> listView;
    private TopBar topBar;

    public CustomListView(TaskManager taskManager, TopBar topBar) {
        this.taskManager = taskManager;
        this.listView = new ListView<>();
        this.topBar = topBar;
    }

    public ListView<T> getListView() {
        return listView;
    }

    public void setupListView(Class<T> type, boolean isSearch) {
        listView.setCellFactory(listView -> new ListCell<T>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button addNotificationButton = new Button("Add Notification");
            private HBox buttonContainer;
            {
                if(type == Task.class) {
                    buttonContainer = new HBox(15, editButton, deleteButton, addNotificationButton);

                }
                else { 
                    buttonContainer = new HBox(15, editButton, deleteButton);
                }
            }
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
                
                addNotificationButton.setOnAction(event -> {
                    Task item = (Task)getItem();
                    if(item.getStatus() == TaskStatus.COMPLETED) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Notification Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Can't add a notification to a completed task.");
                        alert.showAndWait();
                        return; // Exit the method
                    }
                    if (item != null) {
                        new AddNotificationForm(taskManager, (Task) item).showForm();
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
                    String displayText;
                    if (isSearch) {
                        Task task = (Task) item;
                        String categoryName = null;
                        for(Category category : taskManager.getCategories()) {
                            if(category.getId() == task.getCategoryId()) {
                                categoryName = category.getName();
                                break;
                            }
                        }
                        String priorityName = null;
                        for(Priority priority : taskManager.getPriorities()) {
                            if(priority.getId() == task.getPriorityId()) {
                                priorityName = priority.getName();
                                break;
                            }
                        }
            
                        displayText = String.format("Title: %s\nCategory: %s\nPriority: %s\nDate Due: %s",
                        task.getTitle(), categoryName, priorityName, task.getDueDate());
                    } 
                    else {
                        //Default behavior
                        displayText = item.toString();
                    }
                    // Label for item text
                    Label itemLabel = new Label(displayText);
                    itemLabel.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(itemLabel, javafx.scene.layout.Priority.ALWAYS);

                    // New container with item text first, then buttons
                    HBox container = new HBox(10, itemLabel, buttonContainer);
                    setGraphic(container);
                }
            
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void editItem(T item) {
        if (item instanceof Task) {
            new EditTaskForm(taskManager, (Task) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getTasks()));
            topBar.updateLabels();
        } else if (item instanceof Category) {
            new EditCategoryForm((Category) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getCategories()));
        } else if (item instanceof Priority) {
            new EditPriorityForm((Priority) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getPriorities()));
        } else if (item instanceof Notification) {
            new EditNotificationForm((Notification) item).showForm();
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getNotifications()));
        }
    }

    @SuppressWarnings("unchecked")
    private void deleteItem(T item) {
        if (item instanceof Task) {
            taskManager.deleteTask((Task) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getTasks()));
            topBar.updateLabels();
        } else if (item instanceof Category) {
            taskManager.deleteCategory((Category) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getCategories()));
            topBar.updateLabels();
        } else if (item instanceof Priority) {
            taskManager.deletePriority((Priority) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getPriorities()));
        } else if (item instanceof Notification) {
            taskManager.deleteNotification((Notification) item);
            listView.setItems(FXCollections.observableArrayList((List<T>) taskManager.getNotifications()));
        }
    }

    public void setItems(List<T> items) {
        listView.setItems(FXCollections.observableArrayList(items));
    }    
}
