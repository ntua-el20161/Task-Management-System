package taskmanagementsystem;


public class Priority {
    private Integer id;
    private String name;
    
    // Default priority
    public static final Priority Default = new Priority(1, "Default");

    /**
     * Constructor
     * @param name the name of the priority
     */
    public Priority(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the ID of the priority
     * @return the ID of the priority
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the name of the priority
     * @return the name of the priority
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the priority
     * @param name the new name of the priority
     */
    public void setName(String name) {
        this.name = name;
    }
}
