package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import utt.student.greenfresh.R;

public class Status {
    //attributes
    private String lastConnection;
    private Boolean value;
    private String name;

    // getters and setters
    public String getLastConnection() { return lastConnection; }
    public void setLastConnection(String lastConnection) { this.lastConnection = lastConnection; }
    public Boolean getValue() { return value; }
    public void setValue(Boolean value) { this.value = value; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // constructors
    public Status(String lastConnection, Boolean value, String name) {
        this.lastConnection = lastConnection;
        this.value = value;
        this.name = name;
    }

    public Status() {
        this.lastConnection = "";
        this.value = false;
        this.name = "";
    }
}
