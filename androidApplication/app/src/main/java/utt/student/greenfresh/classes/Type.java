package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class Type {
    // attributes
    private String id;
    private String name;
    private Drawable icon;
    private UnitOfMeasurement unitOfMeasurement;

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Drawable getIcon() { return icon; }
    public void setIcon(Drawable icon) { this.icon = icon; }
    public UnitOfMeasurement getUnitOfMeasurement() { return unitOfMeasurement; }
    public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }

    // constructors
    public Type(String id, String name, Drawable icon, UnitOfMeasurement unitOfMeasurement) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Type() {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.unitOfMeasurement = unitOfMeasurement;
    }
}
