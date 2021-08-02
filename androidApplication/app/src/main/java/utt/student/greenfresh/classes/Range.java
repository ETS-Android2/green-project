package utt.student.greenfresh.classes;

public class Range {
    // attributes
    private String name;
    private String color;
    private int maximum;
    private int minimum;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getMaximum() { return maximum; }
    public void setMaximum(int maximum) { this.maximum = maximum; }
    public int getMinimum() { return minimum; }
    public void setMinimum(int minimum) { this.minimum = minimum; }

    // constructors
    public Range(String name, String color, int maximum, int minimum) {
        this.name = name;
        this.color = color;
        this.maximum = maximum;
        this.minimum = minimum;
    }

    public Range() {
        this.name = "";
        this.color = "";
        this.maximum = 0;
        this.minimum = 0;
    }
}
