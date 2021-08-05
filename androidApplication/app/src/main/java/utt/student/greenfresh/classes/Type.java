package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class Type {
    // attributes
    private String name;
    private String icon;
    private UnitOfMeasurement unitOfMeasurement;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public UnitOfMeasurement getUnitOfMeasurement() { return unitOfMeasurement; }
    public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }

    // constructors
    public Type(String name, String icon, UnitOfMeasurement unitOfMeasurement) {
        this.name = name;
        this.icon = icon;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Type() {
        this.name = name;
        this.icon = icon;
        this.unitOfMeasurement = unitOfMeasurement;
    }
}
