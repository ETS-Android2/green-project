package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Status {
    //attributes
    private String lastConnection;
    private Boolean value;
    private String name;
    private Drawable image;

    // getters and setters
    public String getLastConnection() { return lastConnection; }
    public void setLastConnection(String lastConnection) { this.lastConnection = lastConnection; }
    public Boolean getValue() { return value; }
    public void setValue(Boolean value) { this.value = value; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Drawable getImage() { return image; }
    public void setImage(Drawable image) { this.image = image; }

    // constructors
    public Status(String lastConnection, Boolean value, String name, Drawable image) {
        this.lastConnection = lastConnection;
        this.value = value;
        this.name = name;
        this.image = image;
    }

    public Status(String lastConnection, Boolean value, String name) {
        this.lastConnection = lastConnection;
        this.value = value;
        this.name = name;
        this.image = null;
    }

    public Status() {
        this.lastConnection = "";
        this.value = false;
        this.name = "";
        this.image = null;
    }
}
