package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Status {
    //attributes
    private String lastConnection;
    private String value;
    private Drawable image;

    //getters and setters
    public Drawable getImage() {return image;}
    public void setImage(Drawable image) { this.image = image;}
    public String getLastConnection() {return lastConnection; }
    public void setLastConnection(String lastConnection) { this.lastConnection = lastConnection; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }


    //constructor
    public Status() {
        this.lastConnection = "";
        this.value = "";
        this.image = null;
    }

    public Status(String lastConnection, String value) {
        this.lastConnection = lastConnection;
        this.value = value;

    }
}
