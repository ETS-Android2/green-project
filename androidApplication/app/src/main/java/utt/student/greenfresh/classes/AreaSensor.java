package utt.student.greenfresh.classes;

import java.util.ArrayList;

public class AreaSensor {
    // attributes
    private Double current;
    private Type type;
    private ArrayList<Range> ranges;

    // getters and setters
    public Double getCurrent() { return current; }
    public void setCurrent(Double current) { this.current = current; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public ArrayList<Range> getRanges() { return ranges; }
    public void setRanges(ArrayList<Range> ranges) { this.ranges = ranges; }

    // constructors
    public AreaSensor(Double current, Type type, ArrayList<Range> ranges) {
        this.current = current;
        this.type = type;
        this.ranges = ranges;
    }

    public AreaSensor(Double current, Type type) {
        this.current = current;
        this.type = type;
        this.ranges = new ArrayList<>();
    }

    public AreaSensor() {
        this.current = 0.0;
        this.type = new Type();
        this.ranges = new ArrayList<>();
    }

    // methods
    public void addRange(Range range){
        this.ranges.add(range);
    }
}
