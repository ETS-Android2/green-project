package utt.student.greenfresh.classes;

import java.util.ArrayList;

public class FruitSensor {
    // attributes
    private int current;
    private Type type;

    // getters and setters
    public int getCurrent() { return current; }
    public void setCurrent(int current) { this.current = current; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    // constructors
    public FruitSensor(int current, Type type) {
        this.current = current;
        this.type = type;
    }

    public FruitSensor() {
        this.current = 0;
        this.type = new Type();
    }
}
