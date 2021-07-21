package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Status {
    //attributes
    private String lastConnection;
    private String value;


    //getters and setters
    public String getLastConnection() {return lastConnection; }
    public void setLastConnection(String lastConnection) { this.lastConnection = lastConnection; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }


    //constructor
    public Status() {
        this.lastConnection = "";
        this.value = "";
    }

    public Status(String lastConnection, String value) {
        this.lastConnection = lastConnection;
        this.value = value;
    }
}
