package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Status {
    //attributes
    private String name;
    private Drawable icon;

    //getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Drawable getIcon() { return icon; }
    public void setIcon(Drawable icon) { this.icon = icon; }

    //constructors

    public Status() {
        this.name = "";
        this.icon = null;
    }

    public Status(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }
}
