package utt.student.greenfresh.classes;

public class Color {
    // attributes
    private int red;
    private int green;
    private int blue;

    // getters and setters
    public int getRed() { return red; }
    public void setRed(int red) { this.red = red; }
    public int getGreen() { return green; }
    public void setGreen(int green) { this.green = green; }
    public int getBlue() { return blue; }
    public void setBlue(int blue) { this.blue = blue; }

    // constructors
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }
}
