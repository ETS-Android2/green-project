package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class Sensor {
    //atributes
    private String name;
    private Drawable icon;
    private  int value;

    //getters and setters
    //name
    public String getName(){
        return  name;
    }
    public void  setName(String name){
        this.name = name;
    }

    //icon
    public Drawable getIcon(){
        return  icon;
    }
    public void  setIcon(Drawable icon){
        this.icon = icon;
    }

    //value
    public int getValue(){
        return  value;
    }
    public void  setValue(int name){
        this.value = value;
    }

    //constructor
    public void Sensor(){
        this.name = "";
        this.icon = null;
        this.value = 0;
    }

    public Sensor (String name, Drawable icon, int value){
        this.name = name;
        this.icon = icon;
        this.value = value;
    }


    
}
