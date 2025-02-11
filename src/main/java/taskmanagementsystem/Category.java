package taskmanagementsystem;

public class Category {
    // Attributes
    private Integer id;
    private String name;
    
    // Default access: Only the TaskManager can create categories
    Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the ID of the category
     * @return the ID of the category
     */
    public Integer getId() {
        return id;  
    }
    
    /**
     * Get the name of the category
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the category
     * @param name the new name of the category
     */
    public void setName(String name) {
        this.name = name;
    }
}