package utt.student.greenfresh;

public class Fruit {
    //attributes
    private String code;
    private String name;
    private String description;
    private String image;

    //getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    //constructor

    public Fruit() {
        this.code = "";
        this.name = "";
        this.description = "";
        this.image = "";
    }


    public Fruit(String code, String name, String description, String image) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
