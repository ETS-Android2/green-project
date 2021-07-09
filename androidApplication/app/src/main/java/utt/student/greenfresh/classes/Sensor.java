package utt.student.greenfresh.classes;

import android.graphics.drawable.Drawable;

public class Sensor {

    //attributes
    private String name;
    private Drawable icon;



    private  double value;

    //getters and setters

    public String getName(){ return  name; }
    public void  setName(String name){ this.name = name; }

    public Drawable getIcon(){ return  icon; }
    public void  setIcon(Drawable icon){ this.icon = icon; }

    public void setValue(double value) { this.value = value; }
    public double getValue() { return value; }

    //constructor
    public void Sensor(){
        this.name = "";
        this.icon = null;
        this.value = 0.0;
    }

    public Sensor (String name, Drawable icon, double value){
        this.name = name;
        this.icon = icon;
        this.value = value;
    }


}
